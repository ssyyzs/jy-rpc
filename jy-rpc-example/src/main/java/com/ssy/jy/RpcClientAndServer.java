package com.ssy.jy;

import com.ssy.jy.biz.RpcTest;
import com.ssy.jy.config.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-05
 */
public class RpcClientAndServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientAndServer.class);
    public static void main(String[] args) {
        RpcContext context = new RpcContext("jy.yaml");
        RpcTest reference = context.getReference(RpcTest.class);
        int threadSize = args.length > 0 ? Integer.parseInt(args[0]) : 8;
        final int requestPerSecond = args.length > 1 ? Integer.parseInt(args[1]) : 30000;
        int strLen = args.length > 2 ? Math.max(8, Integer.parseInt(args[2])) : 1024;
        String testStr = buildStr(strLen);
        AtomicInteger metrics = new AtomicInteger(0);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadSize, threadSize, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        new Thread(metricTask(metrics), "metric").start();
        new Thread(rpcProducer(requestPerSecond, executor, metrics, reference, testStr), "rpc-producer").start();
    }

    private static String buildStr(int length) {
        StringBuilder testStr = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            testStr.append(Character.toChars('A' + random.nextInt(26)));
        }
        return testStr.toString();
    }

    private static Runnable metricTask(AtomicInteger data) {
        return () -> {
            int pre = 0;
            while (true) {
                int cur = data.get();
                LOGGER.info("tps {}/s", cur - pre);
                pre = cur;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static Runnable rpcProducer(int requestPerSecond, Executor executor, AtomicInteger metrics, RpcTest reference, String data) {
        return () -> {
            for (int i = 0; i < 1800; i++) {
                long start = System.currentTimeMillis();
                for (int j = 0; j < requestPerSecond; j++) {
                    executor.execute(() -> {
                        reference.testStr(data.toString(), Arrays.asList("jy-rpc."));
                        metrics.incrementAndGet();
                    });
                }
                long end = System.currentTimeMillis() - start;
                if (end < 1000) {
                    try {
                        Thread.sleep(1000 - end);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }
}
