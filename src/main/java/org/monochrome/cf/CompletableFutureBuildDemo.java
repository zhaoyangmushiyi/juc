package org.monochrome.cf;

import java.util.concurrent.*;

/**
 * @author monochrome
 * @date 2022/11/24
 */
public class CompletableFutureBuildDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
//        //无返回值
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            System.out.println(Thread.currentThread().getName());
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }, threadPool);
//        System.out.println(completableFuture.get());
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task over";
        }, threadPool);
        System.out.println(completableFuture.get());
        threadPool.shutdown();
    }

}
