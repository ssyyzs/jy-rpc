package com.ssy.jy;

import com.ssy.jy.log.JyLoggerFactory;
import com.ssy.jy.proxy.JdkStubProxyFactory;
import com.ssy.jy.proxy.StubProxyFactory;
import com.ssy.jy.runtime.RpcClientRuntime;
import com.ssy.jy.runtime.RpcServerRuntime;
import com.ssy.jy.transport.*;
import io.netty.channel.Channel;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    private static final Logger LOGGER = JyLoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(3000);
        RpcServerRuntime serverRuntime = new RpcServerRuntime(address);
        RpcClientRuntime clientRuntime = new RpcClientRuntime(address);
        RpcTest proxy = StubProxyFactory.DEFAULT_FACTORY.getProxy(RpcTest.class, clientRuntime);
        System.out.println(proxy.testStr("Hello, ", Arrays.asList("world.")));
        System.out.println(proxy.testList("Hello, ", Arrays.asList("world.")));
    }
}