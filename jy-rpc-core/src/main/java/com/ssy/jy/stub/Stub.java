package com.ssy.jy.stub;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.RpcRuntime;

import java.lang.reflect.Method;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-30
 */
public interface Stub {
    void setRuntime(RpcRuntime runtime);

    RpcRuntime getRuntime();

    Object getRef();

    void setRef(Object ref);

    Class type();

    Object call(Method method, Object[] args) throws RpcException;
}
