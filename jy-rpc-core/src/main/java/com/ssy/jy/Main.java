package com.ssy.jy;

import com.ssy.jy.biz.RpcTest;
import com.ssy.jy.stub.StubFactory;
import com.ssy.jy.runtime.RpcClientRuntime;
import com.ssy.jy.runtime.RpcServerRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(3000);
        RpcServerRuntime serverRuntime = new RpcServerRuntime(address);
        RpcClientRuntime clientRuntime = new RpcClientRuntime(address);
        RpcTest proxy = StubFactory.DEFAULT_FACTORY.getStub(RpcTest.class, clientRuntime);
        System.out.println(proxy.testStr("Hello, ", Arrays.asList("world.")));
        System.out.println(proxy.testList("Hello, ", Arrays.asList("world.")));
    }
}