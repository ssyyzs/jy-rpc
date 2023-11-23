package com.ssy.jy.transport;

import lombok.Data;

/**
 * 请求报文.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
@Data
public class RpcRequestPacket extends Packet {
    private String requestId;
    private String interfaceType;
    private String method;
    private Class[] argumentsType;
    private Object[] arguments;
    private volatile Object result;

    @Override
    public String type() {
        return RpcRequestPacket.class.getName();
    }

    @Override
    public byte command() {
        return Command.REQUEST_COMMAND;
    }

    public synchronized Object syncGet() {
        try {
            if (result == null) {
                wait();
            }
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Object syncGet(long timeoutMillis) {
        try {
            if (result == null) {
                wait(timeoutMillis);
            }
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getImmediately() {
        return result;
    }

    public synchronized void success(Object result) {
        this.result = result;
        notifyAll();
    }
}
