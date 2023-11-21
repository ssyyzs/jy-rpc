package com.ssy.jy.transport;

/**
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class ResponsePacket extends Packet {
    @Override
    public byte command() {
        return Command.REQUEST_RESPONSE_COMMAND;
    }
}
