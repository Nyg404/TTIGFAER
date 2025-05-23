package io.github.nyg404.ttigfaer.core.Manager;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.*;



public class RateLimitManager {
    private final int limit;
    private final int windowSeconds;
    private final Map<Long, Deque<Long>> userCallTimestamps = new ConcurrentHashMap<>();
    private final Map<Long, BlockingQueue<Runnable>> taskQueues = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Executor asyncExecutor;

    public RateLimitManager(int limit, int windowSeconds, Executor asyncExecutor) {
        this.limit = limit;
        this.windowSeconds = windowSeconds;
        this.asyncExecutor = asyncExecutor;
    }

    public void submit(long chatId, Runnable task) {
        taskQueues.computeIfAbsent(chatId, k -> new LinkedBlockingQueue<>());
        BlockingQueue<Runnable> queue = taskQueues.get(chatId);

        synchronized (queue) {
            queue.offer(task);
            processQueue(chatId, queue);
        }
    }

    private void processQueue(long chatId, BlockingQueue<Runnable> queue) {
        Deque<Long> calls = userCallTimestamps.computeIfAbsent(chatId, k -> new ConcurrentLinkedDeque<>());
        long now = System.currentTimeMillis();
        long cutoff = now - windowSeconds * 1000L;

        synchronized (calls) {
            while (!calls.isEmpty() && calls.peekFirst() < cutoff) {
                calls.pollFirst();
            }

            if (calls.size() >= limit) {
                long oldestCallTimestamp = calls.peekFirst();
                long waitTimeMillis = (oldestCallTimestamp + windowSeconds * 1000L) - now;
                if (waitTimeMillis < 0) waitTimeMillis = 0;

                scheduler.schedule(() -> {
                    synchronized (queue) {
                        processQueue(chatId, queue);
                    }
                }, waitTimeMillis, TimeUnit.MILLISECONDS);
                return;
            }


            Runnable nextTask = queue.poll();
            if (nextTask != null) {
                calls.addLast(now);

                asyncExecutor.execute(nextTask);


                scheduler.schedule(() -> {
                    synchronized (queue) {
                        processQueue(chatId, queue);
                    }
                }, 100, TimeUnit.MILLISECONDS);
            }
        }
    }
}
