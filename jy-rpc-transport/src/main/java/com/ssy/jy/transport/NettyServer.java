package com.ssy.jy.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class NettyServer {
    public static void main(String[] args) {
        new NettyServer(new InetSocketAddress(3000)).open();
    }

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
                .option(ChannelOption.SO_BACKLOG, 1)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("splitter", new JyCodecSplitter())
                                .addLast("codec", new JyCodecHandler())
                                .addLast("handler", new SimpleChannelInboundHandler<RequestPacket>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, RequestPacket msg) throws Exception {
                                        System.out.println("server received " + msg);
                                        ResponsePacket responsePacket = new ResponsePacket();
                                        responsePacket.setRequestId(msg.getRequestId());
                                        ctx.channel().writeAndFlush(responsePacket);
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
