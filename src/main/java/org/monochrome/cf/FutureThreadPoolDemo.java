package org.monochrome.cf;

import java.util.concurrent.*;

/**
 * @author monochrome
 * @date 2022/11/23
 */
public class FutureThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // m1();
        // 假定有3个任务，目前有多个异步任务处理
        long startTime = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        FutureTask<String> futureTask1 = new FutureTask<String>(() -> {
            TimeUnit.MILLISECONDS.sleep(100);
            return "task1 over";
        });
        FutureTask<String> futureTask2 = new FutureTask<String>(() -> {
            TimeUnit.MILLISECONDS.sleep(200);
            return "task2 over";
        });
        FutureTask<String> futureTask3 = new FutureTask<String>(() -> {
            TimeUnit.MILLISECONDS.sleep(300);
            return "task3 over";
        });
        threadPool.submit(futureTask1);
        threadPool.submit(futureTask2);
        threadPool.submit(futureTask3);
        System.out.println(futureTask1.get());
        System.out.println(futureTask2.get());
        System.out.println(futureTask3.get());
        long endTime = System.currentTimeMillis();
        System.out.println("----cost Time: " + (endTime - startTime) +"ms");
        System.out.println(Thread.currentThread().getName() + " end");

        threadPool.shutdown();
    }

    private static void m1() throws InterruptedException {
        // 假定有3个任务，目前只有main线程处理
        long startTime = System.currentTimeMillis();
        // 暂停模拟任务
        TimeUnit.MILLISECONDS.sleep(100);
        TimeUnit.MILLISECONDS.sleep(200);
        TimeUnit.MILLISECONDS.sleep(300);
        long endTime = System.currentTimeMillis();
        System.out.println("----cost Time: " + (endTime - startTime) +"ms");
        System.out.println(Thread.currentThread().getName() + " end");
    }

}
