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

    /**
     * 同步获取请求结果.
     *
     * @return 请求结果
     */
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

    /**
     * 带时间的同步获取请求结果.
     *
     * @param timeoutMillis 超时时间.
     * @return 请求结果
     */
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

    /**
     * 立即获取请求结果.
     *
     * @return 请求结果.
     */
    public Object getImmediately() {
        return result;
    }

    /**
     * 设置请求结果.
     *
     * @param result 请求执行结果
     */
    public synchronized void success(Object result) {
        this.result = result;
        notifyAll();
    }
}
