package com.ssy.jy.runtime;

import com.ssy.jy.runtime.transport.*;
import com.ssy.jy.serial.Serializer;
import com.ssy.jy.stub.Stub;
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
import java.util.HashMap;
import java.util.Map;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class RpcClientRuntime implements RpcRuntime, PacketListener<RpcResponsePacket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientRuntime.class);
    private final SocketAddress address;
    private final Bootstrap bootstrap = new Bootstrap();
    private final PacketDispatcher dispatcher = new PacketDispatcher();
    private byte serializerType;
    private final Map<Class<?>, Stub> stubMap = new HashMap<>();

    @Getter
    private Channel clientChannel;

    public RpcClientRuntime(SocketAddress address) {
        this.address = address;
        this.serializerType = Serializer.DEFAULT.type();
        dispatcher.register(this);
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
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("splitter", new JyCodecSplitter())
                                .addLast("codec", new JyCodecHandler())
                                .addLast("handler", new SimpleChannelInboundHandler<Packet>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
                                        dispatcher.dispatch(ctx, msg);
                                    }
                                });
                    }
                });
        clientChannel = bootstrap.connect(address).syncUninterruptibly().channel();
        LOGGER.info("netty client runtime started on: {}", clientChannel);
    }

    @Override
    public void register(Stub stub) {
        stubMap.put(stub.type(), stub);
    }

    @Override
    public JyFuture call(Method method, Object[] args) {
        JyFuture future = RpcRequestGenerator.newRpcRequest(method, args);
        future.getPacket().setSerializerType(serializerType);
        clientChannel.writeAndFlush(future.getPacket());
        return future;
    }

    @Override
    public Class<RpcResponsePacket> interest() {
        return RpcResponsePacket.class;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RpcResponsePacket response) {
        LOGGER.debug("received response {}.", response);
        if (response.isSuccess()) {
            RpcRequestGenerator.success(response.getRequestId(), response.getData());
        } else {
            RpcRequestGenerator.failed(response.getRequestId(), response.getErrorInfo());
        }
    }
}
