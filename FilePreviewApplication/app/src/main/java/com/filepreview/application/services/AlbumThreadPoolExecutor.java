package com.filepreview.application.services;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * thread pool executor
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class AlbumThreadPoolExecutor extends ThreadPoolExecutor {
    private static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static int corePoolSize = Math.max(2, CPU_COUNT);
    private static int maximumPoolSize = corePoolSize << 1;
    private static long keepAliveTime = 1L;
    private static TimeUnit unit = TimeUnit.SECONDS;
    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(128);
    private static RejectedExecutionHandler handler = new DiscardOldestPolicy();
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "AlbumTask #" + mCount.getAndIncrement());
        }
    };

    public AlbumThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public AlbumThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                   ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public AlbumThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public AlbumThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                   ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static AlbumThreadPoolExecutor getDefaultThreadPoolExecutor() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AlbumThreadPoolExecutor INSTANCE = new AlbumThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
}
