package com.ssy.jy.transport;

import io.netty.channel.ChannelHandlerContext;

/**
 * rpc响应报文监听器，处理rpc响应报文.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcResponseListener implements PacketListener<RpcResponsePacket> {
    @Override
    public Class<RpcResponsePacket> interest() {
        return RpcResponsePacket.class;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RpcResponsePacket response) {
        System.out.println(response);
        RpcRequestFactory.getRequest(response.getRequestId()).success(response.getData());
    }
}
