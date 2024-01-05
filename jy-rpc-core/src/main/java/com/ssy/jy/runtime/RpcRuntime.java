package com.ssy.jy.runtime;

import com.ssy.jy.stub.Stub;

import java.lang.reflect.Method;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public interface RpcRuntime {
    void register(Stub stub);

    JyFuture call(Method method, Object[] args);
}
