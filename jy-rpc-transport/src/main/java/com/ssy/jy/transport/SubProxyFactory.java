package com.ssy.jy.transport;

import io.netty.channel.Channel;

import java.lang.reflect.Proxy;

/**
 * 简易代理工厂.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class SubProxyFactory {

    /**
     * 获取rpc代理对象.
     *
     * @param clazz 代理的接口类型
     * @param channel 远程连接
     * @param <T> 代理的接口类型
     * @return 动态代理对象
     */
    public static <T> T newInstance(Class<T> clazz, Channel channel) {
        SubProxy proxy = new SubProxy(clazz, channel);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }
}
