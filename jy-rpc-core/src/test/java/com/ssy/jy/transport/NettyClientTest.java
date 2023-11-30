package com.ssy.jy.transport;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class NettyClientTest {

    @Test
    void open() {
    }
}