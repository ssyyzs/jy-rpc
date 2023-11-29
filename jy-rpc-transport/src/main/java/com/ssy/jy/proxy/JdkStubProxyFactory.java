package com.ssy.jy.proxy;

import com.ssy.jy.runtime.RpcRuntime;
import io.netty.channel.Channel;

import java.lang.reflect.Proxy;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class JdkStubProxyFactory implements StubProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clazz, RpcRuntime runtime) {
        StubProxy proxy = new StubProxy(clazz, runtime);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }
}
