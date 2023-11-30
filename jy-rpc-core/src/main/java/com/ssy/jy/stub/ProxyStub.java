package com.ssy.jy.stub;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.JyFuture;
import com.ssy.jy.runtime.RpcRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk代理对象.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class ProxyStub implements InvocationHandler, Stub {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyStub.class);
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
        JyFuture future = runtime.call(method, args);
        Object result = future.syncGet();
        if (future.isSuccess()) {
            return result;
        }
        throw new RpcException(future.getErrorInfo());
    }

    @Override
    public Class type() {
        return proxyInterface;
    }

    @Override
    public Object call(Method method, Object[] args) throws RpcException {
        try {
            return invoke(this, method, args);
        } catch (Throwable e) {
            LOGGER.error("invoke method error. " + e);
            throw new RpcException(e);
        }
    }
}
