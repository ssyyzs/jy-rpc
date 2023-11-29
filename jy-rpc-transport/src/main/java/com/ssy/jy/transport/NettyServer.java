package com.ssy.jy.transport;

import com.ssy.jy.runtime.RpcClientRuntime;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * netty服务端.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final PacketDispatcher dispatcher = new PacketDispatcher();
    private Channel channel;
    private final SocketAddress address;

    public NettyServer(SocketAddress address) {
        this.address = address;
    }

    /**
     * 建立监听.
     *
     * @return Channel
     */
    public Channel open() {
        if (channel != null) {
            return channel;
        }
        if (address == null) {
            throw new RuntimeException("Unknown connection, because address is null.");
        }
        dispatcher.register(new RpcRequestListener());
        channel = bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .option(ChannelOption.SO_BACKLOG, 1)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("splitter", new JyCodecSplitter())
                                .addLast("codec", new JyCodecHandler())
                                .addLast("handler", new SimpleChannelInboundHandler<Packet>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, Packet msg)
                                            throws Exception {
                                        dispatcher.dispatch(ctx, msg);
                                    }
                                });
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(address).syncUninterruptibly().channel();
        LOGGER.debug("netty server started on: {}", channel);
        return channel;
    }
}
