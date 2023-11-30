package com.ssy.jy.stub;

import com.ssy.jy.exception.RpcException;

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

    private Object object;

    public ServerStub(Class interfaceType, Object object) {
        this.interfaceType = interfaceType;
        this.object = object;
    }

    @Override
    public Class type() {
        return interfaceType;
    }

    @Override
    public Object call(Method method, Object[] args) throws RpcException {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new RpcException("method should be public.");
        } catch (InvocationTargetException e) {
            throw new RpcException(e.getCause());
        }
    }
}
