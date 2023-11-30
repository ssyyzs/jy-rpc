package com.ssy.jy.runtime;

import com.ssy.jy.runtime.transport.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class RpcServerRuntime implements RpcRuntime {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerRuntime.class);
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final PacketDispatcher dispatcher = new PacketDispatcher();
    private Channel serverChannel;
    private SocketAddress address;

    public RpcServerRuntime(InetSocketAddress address) {
        this.address = address;
        dispatcher.register(new RpcRequestListener());
        init();
    }

    private void init() {
        if (address == null) {
            throw new RuntimeException("Unknown connection, because address is null.");
        }
        dispatcher.register(new RpcRequestListener());
        serverChannel = bootstrap.channel(NioServerSocketChannel.class)
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
                                    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
                                        dispatcher.dispatch(ctx, msg);
                                    }
                                });
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(address).syncUninterruptibly().channel();
        LOGGER.debug("netty server started on: {}", serverChannel);
    }

    @Override
    public JyFuture call(Method method, Object[] args) {
        throw new RuntimeException("server runtime is not supported.");
    }
}
