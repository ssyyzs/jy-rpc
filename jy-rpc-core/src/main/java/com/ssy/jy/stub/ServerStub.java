package com.ssy.jy.stub;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.RpcRuntime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-30
 */
public class ServerStub implements Stub {

    private Class interfaceType;
    private RpcRuntime runtime;

    private Object ref;

    public ServerStub(Class interfaceType, RpcRuntime runtime) {
        this.interfaceType = interfaceType;
        this.runtime = runtime;
        runtime.register(this);
    }

    @Override
    public void setRuntime(RpcRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public RpcRuntime getRuntime() {
        return this.runtime;
    }

    @Override
    public Object getRef() {
        return this.ref;
    }

    @Override
    public void setRef(Object ref) {
        this.ref = ref;
    }

    @Override
    public Class type() {
        return interfaceType;
    }

    @Override
    public Object call(Method method, Object[] args) throws RpcException {
        try {
            return method.invoke(ref, args);
        } catch (IllegalAccessException e) {
            throw new RpcException("method should be public.");
        } catch (InvocationTargetException e) {
            throw new RpcException(e.getCause());
        }
    }
}
