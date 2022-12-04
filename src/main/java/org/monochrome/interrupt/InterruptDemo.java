package org.monochrome.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author monochrome
 * @date 2022/11/27
 */
public class InterruptDemo {

    static volatile boolean stopped = false;
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) {
        // interruptByVolatile();
        // interruptByAtomicBoolean();
        interruptByThreadAPI();
    }

    private static void interruptByVolatile() {
        new Thread(() -> {
            while (true) {
                if (stopped) {
                    System.out.println(Thread.currentThread().getName() + "\t stopped is modified to " + stopped);
                    break;
                }
                System.out.println("----hello volatile ");
            }
        }, "t1").start();
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t modifies stopped to true");
            stopped = true;
        }, "t2").start();
    }

    private static void interruptByAtomicBoolean() {
        new Thread(() -> {
            while (true) {
                if (atomicBoolean.get()) {
                    System.out.println(Thread.currentThread().getName() + "\t atomicBoolean is modified to " + atomicBoolean.get());
                    break;
                }
                System.out.println("----hello atomicBoolean ");
            }
        }, "t1").start();
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t modifies atomicBoolean to true");
            atomicBoolean.set(true);
        }, "t2").start();
    }

    /**
     * 在需要中断的线程中不断监听中断状态，一旦发生中断，就执行相应的中断处理业务逻辑stop线程
     */
    private static void interruptByThreadAPI() {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " isInterrupted() is true!");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " hello interrupt api");
            }
        }, "t1");
        t1.start();
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " interrupts t1");
            t1.interrupt();
        }, "t2").start();
    }

}
