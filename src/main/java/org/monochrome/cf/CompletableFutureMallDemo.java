package org.monochrome.cf;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * 案例说明：电商比价需求，模拟如下情况：
 *
 * 1 需求：
 *  1.1 同一款产品，同时搜索出同款产品在各大电商平台的售价;
 *  1.2 同一款产品，同时搜索出本产品在同一个电商平台下，各个入驻卖家售价是多少
 *
 * 2 输出：出来结果希望是同款产品的在不同地方的价格清单列表，返回一个List<String>
 * 《mysql》 in jd price is 88.05
 * 《mysql》 in dangdang price is 86.11
 * 《mysql》 in taobao price is 90.43
 *
 * 3 技术要求
 *   3.1 函数式编程
 *   3.2 链式编程
 *   3.3 Stream流式计算
 *
 * @author monochrome
 * @date 2022/11/25
 */
public class CompletableFutureMallDemo {

    static List<NetMall> netMalls = Arrays.asList(
            new NetMall("jd"),
            new NetMall("tmall"),
            new NetMall("pdd"),
            new NetMall("taobao")
    );

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        List<String> prices = getPrices(netMalls, "mysql");
        prices.forEach(System.out::println);

        long endTime = System.currentTimeMillis();
        System.out.println("Step by Step ----cost Time: " + (endTime - startTime) +"ms");
        long startTime1 = System.currentTimeMillis();

        List<String> pricesByCompletableFuture = getPricesByCompletableFuture(netMalls, "mysql");
        pricesByCompletableFuture.forEach(System.out::println);

        long endTime1 = System.currentTimeMillis();
        System.out.println("Completable Future ----cost Time: " + (endTime1 - startTime1) +"ms");


    }

    /**
     * step by step 一家家搜查
     *
     * @param productName
     * @return
     */
    public static List<String> getPrices(List<NetMall> netMalls, String productName) {
        List<String> prices = netMalls.stream()
                .map(netMall -> String.format("《%s》 in %s price is %.2f", productName, netMall.getMallName(), netMall.calcPrice(productName)))
                .collect(Collectors.toList());
        return prices;

    }

    public static List<String> getPricesByCompletableFuture(List<NetMall> netMalls, String productName) {
        List<String> prices = netMalls.stream()
                .map(netMall -> CompletableFuture.supplyAsync(() -> {
                    return String.format("《%s》 in %s price is %.2f", productName, netMall.getMallName(), netMall.calcPrice(productName));
                }))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return prices;
    }

}

class NetMall {
    @Getter
    private String mallName;

    public NetMall(String mallName) {
        this.mallName = mallName;
    }

    public double calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }

}