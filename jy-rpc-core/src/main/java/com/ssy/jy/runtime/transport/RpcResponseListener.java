package com.ssy.jy.runtime.transport;

import com.ssy.jy.log.JyLoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;

/**
 * rpc响应报文监听器，处理rpc响应报文.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcResponseListener implements PacketListener<RpcResponsePacket> {
    private static final Logger LOGGER = JyLoggerFactory.getLogger(RpcResponseListener.class);

    @Override
    public Class<RpcResponsePacket> interest() {
        return RpcResponsePacket.class;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RpcResponsePacket response) {
        LOGGER.debug("received response {}.", response);
        RpcRequestFactory.getJyFuture(response.getRequestId()).success(response.getData());
    }
}
