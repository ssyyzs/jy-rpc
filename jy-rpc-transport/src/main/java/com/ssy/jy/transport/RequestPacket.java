package com.ssy.jy.transport;

/**
 * 请求报文.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class RequestPacket extends Packet {

    @Override
    public byte command() {
        return Command.REQUEST_COMMAND;
    }
}
