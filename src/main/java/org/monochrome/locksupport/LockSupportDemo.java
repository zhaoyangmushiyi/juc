package org.monochrome.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author monochrome
 * @date 2022/11/27
 */
public class LockSupportDemo {

    public static void main(String[] args) {
        // waitAndNotify();
        // awaitAndSignal();
    }

    private static void awaitAndSignal() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + " was signaled");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " signal");
                condition.signal();
            } finally {
                lock.unlock();
            }

        }, "t2").start();
    }

    private static void waitAndNotify() {
        Object objectLock = new Object();
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + " come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " was notified");
            }
        }, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+" notify");
            }
        }, "t2").start();
    }

}
