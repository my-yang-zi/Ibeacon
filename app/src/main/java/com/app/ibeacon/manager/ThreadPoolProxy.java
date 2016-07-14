package com.app.ibeacon.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建者     伍碧林
 * 创建时间   2016/1/25 16:28
 * 描述	     线程池代理,替线程池完成一些操作
 * 描述	     暴露使用线程池正在需要关心的东西
 * 描述	     使用线程池正在需要关心的东西是啥?
 * 描述	     执行任务,提交任务,移除任务
 * 更新者     $Author: admin $
 * 更新时间   $Date: 2016-01-25 17:08:19 +0800 (星期一, 25 一月 2016) $
 * 更新描述   ${TODO}
 */
public class ThreadPoolProxy {

    ThreadPoolExecutor mExecutor;
    private int mCorePoolSize;
    private int mMaximumPoolSize;


    /**
     * @param corePoolSize    核心池的大小
     * @param maximumPoolSize 最大线程数
     */
    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
    }

    /**
     * 初始化ThreadPoolExecutor
     * 双重检查加锁,只有在第一次实例化的时候才启用同步机制,提高了性能
     */
    private void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    long keepAliveTime = 3000;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue,
                            threadFactory, handler);
                }
            }
        }
    }
    /**
     执行任务和提交任务的区别?
     1.有无返回值
        execute->没有返回值
        submit-->有返回值
     2.Future的具体作用?
        1.有方法可以接收一个任务执行完成之后的结果,其实就是get方法,get方法是一个阻塞方法
        2.get方法的签名抛出了异常===>可以处理任务执行过程中可能遇到的异常
     */
    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.remove(task);
    }
}
