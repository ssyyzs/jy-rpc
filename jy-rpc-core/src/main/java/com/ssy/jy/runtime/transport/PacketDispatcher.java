package com.ssy.jy.runtime.transport;

import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;

/**
 * 报文分发器.
 *
 * @author ssyyzs
 * @since 2023-11-22
 **/
public class PacketDispatcher {
    private Map<String, PacketListener> listenerMap = new HashMap<>();

    public void dispatch(ChannelHandlerContext ctx, Packet msg) {
        listenerMap.get(msg.type()).handle(ctx, msg);
    }

    public void register(PacketListener listener) {
        listenerMap.put(listener.interest().getName(), listener);
    }
}
