package com.ssy.jy.transport;

import io.netty.channel.Channel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk代理对象.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class SubProxy implements InvocationHandler {
    private Channel channel;
    private Class proxyInterface;

    public SubProxy(Class proxyInterface, Channel channel) {
        this.proxyInterface = proxyInterface;
        this.channel = channel;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        RpcRequestPacket requestPacket = RpcRequestFactory.newRpcRequest(method, args);
        channel.writeAndFlush(requestPacket);
        return requestPacket.syncGet();
    }
}
