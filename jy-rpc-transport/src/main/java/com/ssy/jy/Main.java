package com.ssy.jy;

import com.ssy.jy.transport.NettyClient;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        Channel open = new NettyClient(new InetSocketAddress(3000)).open();
        System.out.println(open.isActive());
    }
}