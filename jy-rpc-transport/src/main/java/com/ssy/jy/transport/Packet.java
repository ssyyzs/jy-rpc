package com.ssy.jy.transport;

import lombok.Data;

/**
 * @author y30010171
 * @since 2022-08-24
 **/
@Data
public abstract class Packet {
    private byte version = 1;

    public abstract byte command();
}
