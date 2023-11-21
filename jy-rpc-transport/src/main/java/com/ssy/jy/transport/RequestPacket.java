package com.ssy.jy.transport;

import lombok.Data;

/**
 * 请求报文.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
@Data
public class RequestPacket extends Packet {
    private String requestId;
    private String interfaceType;
    private String method;
    private Object[] arguments;

    @Override
    public byte command() {
        return Command.REQUEST_COMMAND;
    }
}
