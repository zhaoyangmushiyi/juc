package org.monochrome.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author monochrome
 * @date 2022/11/27
 */
public class SaleTicketDemo {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        }, "t1").start();
        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        }, "t2").start();
        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        }, "t3").start();


    }

}


class Ticket {
    private int TOTAL = 50;
    private int number = TOTAL;
    private ReentrantLock lock = new ReentrantLock(true);

    public void sale() {
        lock.lock();
        try {
            if (number > 0) {
                number--;
                System.out.println(Thread.currentThread().getName() + " sales " + (TOTAL - number) + "th ticket, remaining: " + number);
            }
        }finally {
            lock.unlock();
        }
    }

}
