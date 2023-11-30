package com.ssy.jy.runtime.transport;

import com.ssy.jy.runtime.RpcRequestGenerator;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc响应报文监听器，处理rpc响应报文.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcResponseListener implements PacketListener<RpcResponsePacket> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcResponseListener.class);

    @Override
    public Class<RpcResponsePacket> interest() {
        return RpcResponsePacket.class;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, RpcResponsePacket response) {
        LOGGER.debug("received response {}.", response);
        if (response.isSuccess()) {
            RpcRequestGenerator.success(response.getRequestId(), response.getData());
        } else {
            RpcRequestGenerator.failed(response.getRequestId(), response.getErrorInfo());
        }
    }
}
