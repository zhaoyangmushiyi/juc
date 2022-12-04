package org.monochrome.cf;

import java.util.concurrent.*;

/**
 * @author monochrome
 * @date 2022/11/24
 */
public class CompletableFutureUseDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {

            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "---come in");
                int result = ThreadLocalRandom.current().nextInt(10);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 模拟抛异常
                // if (true) {
                //     throw new RuntimeException("get result after 1s wrong!!!");
                // }
                System.out.println("get result after 1s:" + result);
                return result;
            }, threadPool).whenComplete((v, e) -> {
                if (e == null) {
                    System.out.println("successful! get result:" + v);
                }
            }).exceptionally(e -> {
                System.out.println(e.getMessage());
                return null;
            });
        } finally {
            threadPool.shutdown();
        }
        System.out.println(Thread.currentThread().getName() + " do something");
        // 主线程不要立刻结束，否则completableFuture默认使用的线程池会立刻关闭：暂停3秒钟线程
        // try {
        //     TimeUnit.SECONDS.sleep(3);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
    }

    private static void future() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "---come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("get result after 1s:" + result);
            return result;
        });
        System.out.println(Thread.currentThread().getName() + " do something");
        System.out.println(completableFuture.get());
    }
}
