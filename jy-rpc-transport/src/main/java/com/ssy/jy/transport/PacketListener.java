package com.ssy.jy.transport;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author ssyyzs
 * @since 2023-11-22
 **/
public interface PacketListener<T extends Packet> {

    Class<T> interest();
    void handle(ChannelHandlerContext ctx, T t);
}
