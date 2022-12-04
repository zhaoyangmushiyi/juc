package org.monochrome.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author monochrome
 * @date 2022/11/27
 */
public class LockSupportDemo2 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " was unparked");
        }, "t1");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName()+" unparks t1");
        }, "t2").start();

    }

}
