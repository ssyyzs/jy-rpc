package com.ssy.jy.transport;

import lombok.Data;

/**
 * rpc响应报文.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
@Data
public class RpcResponsePacket extends Packet {
    private String requestId;
    private boolean success;
    private Object data;
    private String msg;

    @Override
    public String type() {
        return RpcResponsePacket.class.getName();
    }

    @Override
    public byte command() {
        return Command.REQUEST_RESPONSE_COMMAND;
    }
}
