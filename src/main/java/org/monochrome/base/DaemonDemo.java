package org.monochrome.base;

import java.util.concurrent.TimeUnit;

/**
 * @author monochrome
 * @date 2022/11/23
 */
public class DaemonDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " started, is daemon? " +
                    Thread.currentThread().isDaemon());
            while (true) {

            }
        }, "t1");
        thread.setDaemon(true);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + " stop!");
    }

}
