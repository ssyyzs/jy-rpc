package com.ssy.jy.transport;

import lombok.Data;

/**
 * @author ssyyzs
 * @since 2023-11-21
 **/
@Data
public class ResponsePacket extends Packet {
    private String requestId;

    @Override
    public byte command() {
        return Command.REQUEST_RESPONSE_COMMAND;
    }
}
