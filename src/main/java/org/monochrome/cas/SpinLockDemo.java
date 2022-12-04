package org.monochrome.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 题月：实现一个自旋锁
 * 自旋锁好处：循环比较获取没有类似wait的阻塞。
 * 通这CAS操作完成自旋锁，A线程先进来调用lock方法自己持有锁5秒的，B随后进来后发现
 * 当前线程持有锁，所以只能通过自旋等待，直到A释放锁后B随后抢到。
 * @author monochrome
 * @date 2022/11/30
 */
public class SpinLockDemo {

    AtomicReference<Thread> threadAtomicReference = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "---come in");
        while (!threadAtomicReference.compareAndSet(null, thread)) {
        }
    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        threadAtomicReference.compareAndSet(thread, null);
        System.out.println(thread.getName() + "---task over, unlock");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            spinLockDemo.unlock();
        }, "A").start();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            spinLockDemo.lock();
            spinLockDemo.unlock();
        }, "B").start();

    }


}
