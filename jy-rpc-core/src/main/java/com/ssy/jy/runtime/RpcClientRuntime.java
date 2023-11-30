package com.ssy.jy.runtime;

import com.ssy.jy.runtime.transport.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.SocketAddress;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class RpcClientRuntime implements RpcRuntime {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientRuntime.class);
    private final SocketAddress address;
    private final Bootstrap bootstrap = new Bootstrap();
    private final PacketDispatcher dispatcher = new PacketDispatcher();

    @Getter
    private Channel clientChannel;

    public RpcClientRuntime(SocketAddress address) {
        this.address = address;
        dispatcher.register(new RpcResponseListener());
        init();
    }

    private void init() {
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
                        ch.pipeline()
                                .addLast("splitter", new JyCodecSplitter())
                                .addLast("codec", new JyCodecHandler())
                                .addLast("handler", new SimpleChannelInboundHandler<RpcResponsePacket>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, RpcResponsePacket msg) throws Exception {
                                        dispatcher.dispatch(ctx, msg);
                                    }
                                });
                    }
                });
        clientChannel = bootstrap.connect(address).syncUninterruptibly().channel();
        LOGGER.debug("netty client started on: {}", clientChannel);
    }

    @Override
    public JyFuture call(Method method, Object[] args) {
        RpcRequestPacket requestPacket = RpcRequestFactory.newRpcRequest(method, args);
        JyFuture jyFuture = new JyFuture(requestPacket.getRequestId());
        RpcRequestFactory.register(jyFuture);
        clientChannel.writeAndFlush(requestPacket);
        return jyFuture;
    }
}
