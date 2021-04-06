package me.adjoined.gulimall.ware.thread;


import java.sql.Time;
import java.util.concurrent.*;

public class ThreadTest {
    // should be one to two thread pools for a system.
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main ... starts ...");

//        new Thread(() -> System.out.println("hello")).start();

//        new Thread01().start();

//        new Thread(new Runable01()).start();

//        FutureTask<Integer> task = new FutureTask<>(new Callable01());
//        new Thread(task).start();
//        System.out.println("get task result: " + task.get());

//        executorService.execute(new Runable01());
        // 原生
        /*
        Params:
            corePoolSize – the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set
            maximumPoolSize – the maximum number of threads to allow in the pool
            keepAliveTime – when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
            unit – the time unit for the keepAliveTime argument
            workQueue – the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
            threadFactory – the factory to use when the executor creates a new thread
            handler – the handler to use when execution is blocked because the thread bounds and queue capacities are reached
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
//        Executors.newFixedThreadPool()
//        Executors.newCachedThreadPool()
//        Executors.newScheduledThreadPool()   schedule job in the future.


        System.out.println("main ... ends ...");

    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("current thread: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("result: " + i);
        }
    }

    public static class Runable01 implements Runnable {

        @Override
        public void run() {
            System.out.println("current runnable thread: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("result: " + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("current callable thread: " + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("result: " + i);
            return i;
        }
    }
}
