package org.monochrome.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 以一种线程安全的方式操作非线程安全对象的某些字段。
 * 需求：
 * 10个线程，
 * 每个线程转账1000，
 * 不使用synchronized，尝武使用AtomicIntegerFieldUpdater来实现。
 * @author monochrome
 * @date 2022/12/2
 */
public class AtomicIntegerFieldUpdaterDemo {

    final static int SIZE = 10;
    static CountDownLatch countDownLatch = new CountDownLatch(SIZE);

    public static void main(String[] args) throws InterruptedException {
        BankAccount bankAccount = new BankAccount();

        for (int i = 0; i < SIZE; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        bankAccount.safeAdd(bankAccount, 1);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + i).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " money:" + bankAccount.money);

    }

}

class BankAccount {

    String bankName = "CCB";
    public volatile int money = 0;

    // 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必领
    // 使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
    static final AtomicIntegerFieldUpdater<BankAccount> fieldUpdater =
            AtomicIntegerFieldUpdater.newUpdater(BankAccount.class, "money");

    public synchronized void add() {
        money++;
    }

    public synchronized void safeAdd(BankAccount bankAccount, int delta) {
        fieldUpdater.getAndAdd(bankAccount, delta);
    }


}
