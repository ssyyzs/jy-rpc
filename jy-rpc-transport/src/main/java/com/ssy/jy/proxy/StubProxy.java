package com.ssy.jy.proxy;

import com.ssy.jy.runtime.RpcRuntime;
import com.ssy.jy.transport.RpcRequestFactory;
import com.ssy.jy.transport.RpcRequestPacket;
import io.netty.channel.Channel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk代理对象.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class StubProxy implements InvocationHandler {
    private RpcRuntime runtime;
    private Class proxyInterface;

    public StubProxy(Class proxyInterface, RpcRuntime runtime) {
        this.proxyInterface = proxyInterface;
        this.runtime = runtime;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        RpcRequestPacket requestPacket = RpcRequestFactory.newRpcRequest(method, args);
        runtime.call(requestPacket);
        return requestPacket.syncGet();
    }
}
