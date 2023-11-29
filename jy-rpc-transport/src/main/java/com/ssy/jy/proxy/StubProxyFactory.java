package com.ssy.jy.proxy;

import com.ssy.jy.runtime.RpcRuntime;
import io.netty.channel.Channel;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public interface StubProxyFactory {
    StubProxyFactory DEFAULT_FACTORY = new JdkStubProxyFactory();
    
    /**
     * 获取rpc代理对象.
     *
     * @param clazz 代理的接口类型
     * @param runtime 远程连接
     * @param <T> 代理的接口类型
     * @return 动态代理对象
     */
    <T> T getProxy(Class<T> clazz, RpcRuntime runtime);
}
