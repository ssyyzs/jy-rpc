package com.ssy.jy.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;

/**
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class NettyServer {

    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private Channel channel;
    private SocketAddress address;

    public NettyServer(SocketAddress address) {
        this.address = address;
    }

    public Channel open() {
        if (address == null) {
            throw new RuntimeException("Unknown connection, because address is null.");
        }
        channel = bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        System.out.println("init new channel: " + ch);
                    }
                })
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast("hello", new SimpleChannelInboundHandler<>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(address).syncUninterruptibly().channel();
        return channel;
    }
}
