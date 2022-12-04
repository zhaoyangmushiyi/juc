package org.monochrome.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.stream.IntStream;

/**
 * @author monochrome
 * @date 2022/12/2
 */
public class AtomicReferenceFieldUpdaterDemo {

    static Resource resource = new Resource();
    final static int SIZE = 10;
    static CountDownLatch countDownLatch = new CountDownLatch(SIZE);

    public static void main(String[] args) throws InterruptedException {

        IntStream.range(0, SIZE).parallel().forEach(value -> {
            new Thread(() -> {
                try {
                    resource.init(resource);
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + value).start();
        });
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " finish");
    }

}

class Resource {

    public volatile Boolean init = Boolean.FALSE;

    static final AtomicReferenceFieldUpdater<Resource, Boolean> referenceFieldUpdater =
            AtomicReferenceFieldUpdater.newUpdater(Resource.class, Boolean.class, "init");

    public void init(Resource resource) {
        if (referenceFieldUpdater.compareAndSet(resource, Boolean.FALSE, Boolean.TRUE)) {
            System.out.println(Thread.currentThread().getName() + " starts init");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " ends init");
        } else {
            System.out.println(Thread.currentThread().getName()+"----there is a thread to start init");
        }
    }

}
