package com.ssy.jy.runtime.transport;

import com.ssy.jy.biz.RpcTest;
import com.ssy.jy.biz.RpcTestImpl;
import com.ssy.jy.exception.RpcException;
import com.ssy.jy.stub.ServerStub;
import com.ssy.jy.stub.Stub;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc请求监听器，处理收到的rpc请求.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcRequestListener implements PacketListener<RpcRequestPacket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRequestListener.class);
    Map<Class, Stub> interfaceImplMap = new HashMap<>();

    {
        Stub serverStub = new ServerStub(RpcTest.class, new RpcTestImpl());
        interfaceImplMap.put(serverStub.type(), serverStub);
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
            Stub stub = interfaceImplMap.get(targetClass);
            Method targetMethod = targetClass.getDeclaredMethod(request.getMethod(), request.getArgumentsType());
            Object result = stub.call(targetMethod, request.getArguments());
            response.setSuccess(true);
            response.setData(result);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.error("interfaceType or method not existed. ", e);
            response.setErrorInfo("interfaceType or method not existed.");
        } catch (RpcException e) {
            LOGGER.error("call method error. requestId: {}", request.getRequestId(), e);
            response.setErrorInfo("invalid method. " + e.getMessage());
        } finally {
            ctx.channel().writeAndFlush(response);
        }
    }
}
