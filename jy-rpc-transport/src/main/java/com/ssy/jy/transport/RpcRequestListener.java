package com.ssy.jy.transport;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcRequestListener implements PacketListener<RpcRequestPacket> {
    Map<String, Object> interfaceImplMap = new HashMap<>();

    {
        interfaceImplMap.put(RpcTest.class.getName(), new RpcTestImpl());
    }

    @Override
    public Class<RpcRequestPacket> interest() {
        return RpcRequestPacket.class;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RpcRequestPacket request) {
        Object interfaceImpl = interfaceImplMap.get(request.getInterfaceType());
        RpcResponsePacket response = new RpcResponsePacket();
        response.setRequestId(request.getRequestId());
        try {
            Class<?> targetClass = Class.forName(request.getInterfaceType());
            Method targetMethod = targetClass.getDeclaredMethod(request.getMethod(), request.getArgumentsType());
            Object result = targetMethod.invoke(interfaceImpl, request.getArguments());
            response.setSuccess(true);
            response.setData(result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.setMsg("invalid interfaceType.");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            response.setMsg("invalid method.");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            response.setMsg("invalid argument.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response.setMsg("method should be public.");
        } finally {
            ctx.channel().writeAndFlush(response);
        }
    }
}
