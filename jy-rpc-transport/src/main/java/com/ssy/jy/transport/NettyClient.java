package com.ssy.jy.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

/**
 * netty 客户端.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class NettyClient {

    private SocketAddress address;
    private final Bootstrap bootstrap = new Bootstrap();
    private Channel channel;

    public NettyClient(SocketAddress address) {
        this.address = address;
    }

    /**
     * 和对端server建立tcp连接.
     *
     * @return Channel
     */
    public Channel open() {
        if (address == null) {
            throw new RuntimeException("Unknown connection, because address is null.");
        }
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("hello", new SimpleChannelInboundHandler<>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                });
        try {
            channel = bootstrap.connect(address).await().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return channel;
    }
}
