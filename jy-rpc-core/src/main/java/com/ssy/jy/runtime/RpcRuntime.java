package com.ssy.jy.runtime;

import java.lang.reflect.Method;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public interface RpcRuntime {

    JyFuture call(Method method, Object[] args);
}
