package com.ssy.jy.stub;

import com.ssy.jy.exception.RpcException;

import java.lang.reflect.Method;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-30
 */
public interface Stub {

    Class type();
    Object call(Method method, Object[] args) throws RpcException;
}
