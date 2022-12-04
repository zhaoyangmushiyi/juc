package org.monochrome.cf;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author monochrome
 * @date 2022/11/23
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(new MyThread());
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

class MyThread implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("come in call()");
        return "hello callable";
    }
}