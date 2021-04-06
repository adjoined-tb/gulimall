package me.adjoined.gulimall.ware.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureTest {
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main ... starts ...");
//        CompletableFuture.runAsync(()->{
//            System.out.println("current runnable thread: " + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("result: " + i);
//        }, executorService);

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("current runnable thread: " + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("result: " + i);
//            return i;
//        }, executorService).whenComplete((result, exception)-> {
//            System.out.println("first task succeeded: " + result + "; exception: " + exception);
//        }).exceptionally(throwable -> {
//            return 10;
//        });
//
//        System.out.println("future is " + future.get());


//
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("current runnable thread: " + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("result: " + i);
//            return i;
//        }, executorService).handle((result, exception) -> {
//            if (result != null) {
//                return result * 2;
//            }
//            if (exception != null) {
//                return -1;
//            }
//            return 0;
//        });
//        System.out.println("future is " + future.get());


        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("current runnable thread: " + Thread.currentThread().getId());
            int i = 10 / 0; //2
            System.out.println("result: " + i);
            return i;
        }, executorService);
//        CompletableFuture<Void> future2 = future1.thenRunAsync(() -> {
//            System.out.println("then run");
//        }, executorService);
//
//        CompletableFuture<Void> future3 = future1.thenAcceptAsync(result -> {
//            System.out.println("got resutl: " + result);
//        }, executorService);

        CompletableFuture<Integer> future4 = future1.thenApplyAsync(res -> {
            System.out.println("got resutl: " + res);
            return 100;
        }, executorService);
        CompletableFuture<Integer> future40 = future4.whenCompleteAsync((v, e) -> {
            if (v != null) {
                System.out.println("f4, get v: " + v);
            }
            if (e != null) {
                System.out.println("f4, get e: " + e);
            }
        }).exceptionally(t -> {
            System.out.println("yes, error: " + t);
            return 10000;
        });
        System.out.println("future40 is " + future40.get());

//        CompletableFuture<Integer> future10 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("current runnable thread: " + Thread.currentThread().getId());
//            int i = 10 / 10; //0
//            System.out.println("result: " + i);
//            return i;
//        }, executorService);
//
//        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future10);
//        allFutures.get();
        System.out.println("main ... ends ...");
    }
}
