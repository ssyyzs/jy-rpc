package com.ssy.jy.runtime;

import com.ssy.jy.runtime.transport.Packet;
import lombok.Getter;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-30
 */
public class JyFuture {

    @Getter
    private String requestId;
    private volatile Object result;

    @Getter
    private volatile boolean success;
    @Getter
    private volatile String errorInfo;
    @Getter
    private Packet packet;

    public JyFuture(String requestId, Packet packet) {
        this.requestId = requestId;
        this.packet = packet;
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
        this.success = true;
        this.result = result;
        notifyAll();
    }

    public synchronized void failed(String msg) {
        this.errorInfo = msg;
        notifyAll();
    }
}
