package com.ssy.jy.runtime.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 定长报文解析.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class JyCodecSplitter extends LengthFieldBasedFrameDecoder {
    public JyCodecSplitter() {
        super(Integer.MAX_VALUE, 7, 4);
    }
}
