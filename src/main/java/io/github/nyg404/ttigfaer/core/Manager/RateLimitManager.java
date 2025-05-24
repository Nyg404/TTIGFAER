package io.github.nyg404.ttigfaer.core.Manager;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Менеджер для ограничения частоты выполнения задач (Rate Limiting) по каждому chatId.
 *
 * <p>Позволяет ограничивать количество выполняемых задач в заданный временной интервал (окно).
 * Задачи ставятся в очередь и выполняются асинхронно с учётом ограничения.</p>
 */
public class RateLimitManager {
    private final int limit; // Максимальное количество вызовов в окне
    private final int windowSeconds; // Размер окна в секундах
    private final Map<Long, Deque<Long>> userCallTimestamps = new ConcurrentHashMap<>();
    private final Map<Long, BlockingQueue<Runnable>> taskQueues = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Executor asyncExecutor;

    /**
     * Конструктор менеджера ограничения частоты.
     *
     * @param limit максимальное число вызовов за окно времени
     * @param windowSeconds длительность окна в секундах
     * @param asyncExecutor исполнитель для асинхронного выполнения задач
     */
    public RateLimitManager(int limit, int windowSeconds, Executor asyncExecutor) {
        this.limit = limit;
        this.windowSeconds = windowSeconds;
        this.asyncExecutor = asyncExecutor;
    }

    /**
     * Добавляет задачу в очередь на выполнение с учётом ограничения частоты для данного chatId.
     *
     * @param chatId идентификатор чата, для которого применяется ограничение
     * @param task задача для выполнения
     */
    public void submit(long chatId, Runnable task) {
        taskQueues.computeIfAbsent(chatId, k -> new LinkedBlockingQueue<>());
        BlockingQueue<Runnable> queue = taskQueues.get(chatId);

        synchronized (queue) {
            queue.offer(task);
            processQueue(chatId, queue);
        }
    }

    /**
     * Обрабатывает очередь задач для chatId, выполняя задачи, если ограничение не превышено.
     * Если лимит достигнут, планирует повторную попытку через необходимую задержку.
     *
     * @param chatId идентификатор чата
     * @param queue очередь задач для данного chatId
     */
    private void processQueue(long chatId, BlockingQueue<Runnable> queue) {
        Deque<Long> calls = userCallTimestamps.computeIfAbsent(chatId, k -> new ConcurrentLinkedDeque<>());
        long now = System.currentTimeMillis();
        long cutoff = now - windowSeconds * 1000L;

        synchronized (calls) {
            // Удаляем устаревшие отметки времени (вне окна)
            while (!calls.isEmpty() && calls.peekFirst() < cutoff) {
                calls.pollFirst();
            }

            if (calls.size() >= limit) {
                // Лимит достигнут — планируем запуск после окончания окна
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

            // Лимит не превышен — выполняем следующую задачу
            Runnable nextTask = queue.poll();
            if (nextTask != null) {
                calls.addLast(now);

                asyncExecutor.execute(nextTask);

                // Планируем обработку очереди через небольшую задержку (100 мс)
                scheduler.schedule(() -> {
                    synchronized (queue) {
                        processQueue(chatId, queue);
                    }
                }, 100, TimeUnit.MILLISECONDS);
            }
        }
    }
}
