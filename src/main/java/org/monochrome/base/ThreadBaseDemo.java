package org.monochrome.base;

/**
 * @author monochrome
 * @date 2022/11/23
 */
public class ThreadBaseDemo {

    public static void main(String[] args) {
        new Thread(() -> {

        }, "t1").start();
    }

}
