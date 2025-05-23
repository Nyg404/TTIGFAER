package io.github.nyg404.ttigfaer.core.Manager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

public class RateLimitManager {

    private final int maxCalls;
    private final long intervalMillis;

    // Для каждого чата очередь вызовов
    private final Map<Long, Queue<Runnable>> queues = new ConcurrentHashMap<>();
    // Для каждого чата — время вызовов в пределах интервала (FIFO)
    private final Map<Long, Deque<Long>> callTimestamps = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public RateLimitManager(int maxCalls, long intervalMillis) {
        this.maxCalls = maxCalls;
        this.intervalMillis = intervalMillis;
        scheduler.scheduleAtFixedRate(this::processQueues, 0, 100, TimeUnit.MILLISECONDS);
    }

    // Метод добавляет вызов либо сразу запускает, либо ставит в очередь
    public void submit(long chatId, Runnable task) {
        queues.putIfAbsent(chatId, new ConcurrentLinkedQueue<>());
        callTimestamps.putIfAbsent(chatId, new ArrayDeque<>());

        synchronized (getLock(chatId)) {
            if (canExecute(chatId)) {
                recordCall(chatId);
                task.run();
            } else {
                queues.get(chatId).offer(task);
            }
        }
    }

    // Проверяем, можно ли сейчас вызвать обработчик для чата
    private boolean canExecute(long chatId) {
        Deque<Long> timestamps = callTimestamps.get(chatId);
        long now = System.currentTimeMillis();

        // Удаляем устаревшие таймстампы
        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > intervalMillis) {
            timestamps.pollFirst();
        }
        return timestamps.size() < maxCalls;
    }

    private void recordCall(long chatId) {
        Deque<Long> timestamps = callTimestamps.get(chatId);
        timestamps.addLast(System.currentTimeMillis());
    }

    // Периодически пытаемся выполнить задачи из очереди
    private void processQueues() {
        for (Long chatId : queues.keySet()) {
            synchronized (getLock(chatId)) {
                Queue<Runnable> queue = queues.get(chatId);
                if (queue == null) continue;

                while (!queue.isEmpty() && canExecute(chatId)) {
                    Runnable task = queue.poll();
                    if (task != null) {
                        recordCall(chatId);
                        task.run();
                    }
                }
            }
        }
    }

    // Для синхронизации по chatId
    private final Map<Long, Object> locks = new ConcurrentHashMap<>();
    private Object getLock(Long chatId) {
        locks.putIfAbsent(chatId, new Object());
        return locks.get(chatId);
    }
}

