package org.monochrome.locks;

import java.util.concurrent.TimeUnit;

/**
 * @author monochrome
 * @date 2022/11/27
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        Object objectA = new Object();
        Object objectB = new Object();

        new Thread(() -> {
            synchronized (objectA) {
                System.out.println(Thread.currentThread().getName()+" got objectA, try to get objectB");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (objectB) {
                    System.out.println(Thread.currentThread().getName()+" got objectB");
                }
            }
        }, "t1").start();
        new Thread(() -> {
            synchronized (objectB) {
                System.out.println(Thread.currentThread().getName()+" got objectB, try to get objectA");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (objectA) {
                    System.out.println(Thread.currentThread().getName()+" got objectA");
                }
            }
        }, "t2").start();

    }
}
