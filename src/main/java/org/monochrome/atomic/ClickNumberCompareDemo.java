package org.monochrome.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * 需求：50个线程，每个线程100w次，总点费数出夹
 * @author monochrome
 * @date 2022/12/2
 */
public class ClickNumberCompareDemo {


    static NumberClick numberClick = new NumberClick();
    static final int THREAD_NUM = 50;
    static final int TIMES = 1000000;

    public static void main(String[] args) throws InterruptedException {
        clickBySynchronized();
        clickByAtomicLong();
        clickByLongAdder();
        clickByLongAccumulator();
    }

    private static void clickByLongAccumulator() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        IntStream.range(0, THREAD_NUM).parallel().forEach((value) -> {
            new Thread(() -> {
                try {
                    for (int i = 0; i < TIMES; i++) {
                        numberClick.clickByLongAccumulator();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + value).start();
        });
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("----LongAccumulator total click: " + numberClick.longAccumulator.get());
        System.out.println("----LongAccumulator cost Time: " + (endTime - startTime) + "ms");
    }
    private static void clickByLongAdder() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        IntStream.range(0, THREAD_NUM).parallel().forEach((value) -> {
            new Thread(() -> {
                try {
                    for (int i = 0; i < TIMES; i++) {
                        numberClick.clickByLongAdder();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + value).start();
        });
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("----LongAdder total click: " + numberClick.longAdder.sum());
        System.out.println("----LongAdder cost Time: " + (endTime - startTime) + "ms");
    }

    private static void clickByAtomicLong() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        IntStream.range(0, THREAD_NUM).parallel().forEach((value) -> {
            new Thread(() -> {
                try {
                    for (int i = 0; i < TIMES; i++) {
                        numberClick.clickByAtomicLong();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + value).start();
        });
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("----AtomicLong total click: " + numberClick.atomicLong.get());
        System.out.println("----AtomicLong cost Time: " + (endTime - startTime) + "ms");
    }

    private static void clickBySynchronized() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        IntStream.range(0, THREAD_NUM).parallel().forEach((value) -> {
            new Thread(() -> {
                try {
                    for (int i = 0; i < TIMES; i++) {
                        numberClick.clickBySynchronized();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + value).start();
        });
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("----synchronized total click: " + numberClick.number);
        System.out.println("----synchronized cost Time: " + (endTime - startTime) + "ms");
    }

}

class NumberClick {

    int number = 0;
    AtomicLong atomicLong = new AtomicLong();
    LongAdder longAdder = new LongAdder();
    LongAccumulator longAccumulator = new LongAccumulator(Long::sum, 0);
    public synchronized void clickBySynchronized() {
        number++;
    }

    public void clickByAtomicLong() {
        atomicLong.getAndIncrement();
    }

    public void clickByLongAdder() {
        longAdder.increment();
    }
    public void clickByLongAccumulator() {
        longAccumulator.accumulate(1);
    }

}
