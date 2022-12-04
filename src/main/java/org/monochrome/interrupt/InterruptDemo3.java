package org.monochrome.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @author monochrome
 * @date 2022/11/27
 */
public class InterruptDemo3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " isInterrupted() is true!");
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //线程的中断标志位重新设置为false,无法停下,需要再次掉interrupt()设置true
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " hello InterruptDemo3");
            }
        }, "t1");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " interrupts t1");
            t1.interrupt();
        }, "t2").start();
    }

}
