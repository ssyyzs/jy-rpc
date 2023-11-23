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

    public static <T> T newInstance(Class<T> clazz, Channel channel) {
        SubProxy proxy = new SubProxy(clazz, channel);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }
}
