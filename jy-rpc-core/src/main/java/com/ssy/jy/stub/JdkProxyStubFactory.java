package com.ssy.jy.stub;

import com.ssy.jy.runtime.RpcRuntime;

import java.lang.reflect.Proxy;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class JdkProxyStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getStub(Class<T> clazz, RpcRuntime runtime) {
        ProxyStub proxy = new ProxyStub(clazz, runtime);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }
}
