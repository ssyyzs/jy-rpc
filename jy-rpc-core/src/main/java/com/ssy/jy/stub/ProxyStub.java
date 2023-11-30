package com.ssy.jy.stub;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.RpcRuntime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk代理对象.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class ProxyStub implements InvocationHandler, Stub {
    private RpcRuntime runtime;
    private Class proxyInterface;

    public ProxyStub(Class proxyInterface, RpcRuntime runtime) {
        this.proxyInterface = proxyInterface;
        this.runtime = runtime;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        return runtime.call(method, args).syncGet();
    }

    @Override
    public Class type() {
        return proxyInterface;
    }

    @Override
    public Object call(Method method, Object[] args)  throws RpcException {
        try {
            return invoke(this, method, args);
        } catch (Throwable e) {
            throw new RpcException(e);
        }
    }
}
