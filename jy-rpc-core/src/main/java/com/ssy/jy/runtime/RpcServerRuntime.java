package com.ssy.jy.runtime;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.transport.*;
import com.ssy.jy.stub.ServerStub;
import com.ssy.jy.stub.Stub;
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
import java.util.HashMap;
import java.util.Map;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class RpcServerRuntime implements RpcRuntime, PacketListener<RpcRequestPacket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerRuntime.class);

    private Map<Class, Stub> stubMap = new HashMap<>();

    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final PacketDispatcher dispatcher = new PacketDispatcher();
    private Channel serverChannel;
    private SocketAddress address;

    public RpcServerRuntime(InetSocketAddress address) {
        this.address = address;
        dispatcher.register(this);
        init();
    }

    private void init() {
        if (address == null) {
            throw new RuntimeException("Unknown connection, because address is null.");
        }
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
        LOGGER.info("netty server runtime started on: {}", serverChannel);
    }

    @Override
    public void register(Stub stub) {
        stubMap.put(stub.type(), stub);
    }

    @Override
    public JyFuture call(Method method, Object[] args) {
        throw new RuntimeException("server runtime is not supported.");
    }

    @Override
    public Class<RpcRequestPacket> interest() {
        return RpcRequestPacket.class;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RpcRequestPacket request) {
        RpcResponsePacket response = new RpcResponsePacket();
        response.setRequestId(request.getRequestId());
        try {
            Class<?> targetClass = Class.forName(request.getInterfaceType());
            Stub stub = stubMap.get(targetClass);
            Method targetMethod = targetClass.getDeclaredMethod(request.getMethod(), request.getArgumentsType());
            Object result = stub.call(targetMethod, request.getArguments());
            response.setSuccess(true);
            response.setData(result);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.error("interfaceType or method not existed. ", e);
            response.setErrorInfo("interfaceType or method not existed.");
        } catch (RpcException e) {
            LOGGER.error("call method error. requestId: {}", request.getRequestId(), e);
            response.setErrorInfo(e.getMessage());
        } finally {
            ctx.channel().writeAndFlush(response);
        }
    }

    public void registerService(Class clazz, Object ref) {
        Stub serverStub = new ServerStub(clazz, this);
        serverStub.setRef(ref);
        stubMap.put(serverStub.type(), serverStub);
    }
}
