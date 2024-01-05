package com.ssy.jy.stub;

import com.ssy.jy.runtime.RpcRuntime;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public interface StubFactory {
    /**
     * 获取rpc代理对象.
     *
     * @param clazz 代理的接口类型
     * @param runtime 远程连接
     * @param <T> 代理的接口类型
     * @return 动态代理对象
     */
    <T> T getStub(Class<T> clazz, RpcRuntime runtime);
}
