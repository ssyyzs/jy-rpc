package com.ssy.jy.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 出入站编码处理器，将出站packet变成ByteBuf，将入站ByteBuf变成Packet.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class JyCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> list) throws Exception {
        list.add(JyCodec.encode(ctx.alloc().buffer(), packet));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(JyCodec.decode(byteBuf));
    }
}
