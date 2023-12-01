package com.ssy.jy;

import com.ssy.jy.biz.RpcTest;
import com.ssy.jy.biz.RpcTestImpl;
import com.ssy.jy.runtime.RpcClientRuntime;
import com.ssy.jy.runtime.RpcServerRuntime;
import com.ssy.jy.stub.StubFactory;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        String mode = "all";
        if (args.length > 0) {
            mode = args[0];
        }
        int port = 3000;
        if (args.length > 1) {
            port = Integer.valueOf(args[1]);
        }
        String hostName = "localhost";
        if (args.length > 2) {
            hostName = args[2];
        }
        int threadSize = 8;
        if (args.length > 3) {
            threadSize = Integer.valueOf(args[3]);
        }
        final int requestPerSecond;
        if (args.length > 4) {
            requestPerSecond = Integer.valueOf(args[4]);
        } else {
            requestPerSecond = 10000;
        }
        RpcClientRuntime runtime = null;
        if ("client".equals(mode)) {
            runtime = client(hostName, port);
        } else if ("server".equals(mode)) {
            server(port);
        } else {
            server(port);
            runtime = client(hostName, port);
        }
        if (runtime != null) {
            RpcTest proxy = StubFactory.DEFAULT_FACTORY.getStub(RpcTest.class, runtime);
            AtomicInteger count = new AtomicInteger(0);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(threadSize, threadSize, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
            Runnable metricTask = () -> {
                int pre = 0;
                while (true) {
                    int cur = count.get();
                    System.err.println(LocalDateTime.now() + ": " + (cur - pre));
                    pre = cur;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            Runnable rpcProducer = () -> {
                for (int i = 0; i < 1800; i++) {
                    long start = System.currentTimeMillis();
                    for (int j = 0; j < requestPerSecond; j++) {
                        executor.execute(() -> {
                            proxy.testStr("Hello, ", Arrays.asList("jy-rpc."));
                            count.incrementAndGet();
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
            new Thread(metricTask, "metric").start();
            new Thread(rpcProducer, "rpc-producer").start();
        }
    }

    public static RpcClientRuntime client(String host, int port) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        RpcClientRuntime runtime = new RpcClientRuntime(address);
        return runtime;
    }

    public static RpcServerRuntime server(int port) {
        InetSocketAddress address = new InetSocketAddress(port);
        RpcServerRuntime runtime = new RpcServerRuntime(address);
        runtime.registerService(RpcTest.class, new RpcTestImpl());
        return runtime;
    }
}