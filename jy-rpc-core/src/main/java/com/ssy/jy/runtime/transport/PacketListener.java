package com.ssy.jy.runtime.transport;

import io.netty.channel.ChannelHandlerContext;

/**
 * 报文监听器，处理对应报文.
 *
 * @author ssyyzs
 * @since 2023-11-22
 **/
public interface PacketListener<T extends Packet> {

    Class<T> interest();

    void handle(ChannelHandlerContext ctx, T t);
}
