package com.ssy.jy.runtime.transport;

import lombok.Data;

/**
 * 传输报文对象.
 *
 * @author y30010171
 * @since 2022-08-24
 **/
@Data
public abstract class Packet {
    private byte version = 1;

    public abstract String type();

    public abstract byte command();

    static interface Command {

        byte REQUEST_COMMAND = 1;
        byte REQUEST_RESPONSE_COMMAND = 2;
    }
}
