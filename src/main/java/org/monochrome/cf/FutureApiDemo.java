package org.monochrome.cf;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author monochrome
 * @date 2022/11/23
 */
public class FutureApiDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + " come in");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "task over";
        });

        Thread thread = new Thread(futureTask, "t1");
        thread.start();
        System.out.println(Thread.currentThread().getName() + " do some thing");
//        System.out.println(futureTask.get());
//        System.out.println(futureTask.get(3, TimeUnit.SECONDS));
        while (true) {
            if (futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println("future is doing");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
