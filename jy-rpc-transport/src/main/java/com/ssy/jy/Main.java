package com.ssy.jy;

import com.ssy.jy.transport.*;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        InetSocketAddress address = new InetSocketAddress(3000);
        Channel serverChannel = new NettyServer(address).open();
        System.out.println(serverChannel);
        Channel clientChannel = new NettyClient(address).open();
        System.out.println(clientChannel);
        RpcTest proxy = SubProxyFactory.newInstance(RpcTest.class, clientChannel);
        System.out.println(proxy.testStr("Hello, ", Arrays.asList("world.")));
        System.out.println(proxy.testList("Hello, ", Arrays.asList("world.")));
    }
}