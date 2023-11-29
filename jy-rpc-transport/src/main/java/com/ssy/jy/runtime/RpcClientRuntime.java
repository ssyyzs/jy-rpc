package com.ssy.jy.runtime;

import com.ssy.jy.transport.NettyClient;
import lombok.Data;
import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class RpcClientRuntime implements RpcRuntime {
    private NettyClient client;
    public RpcClientRuntime(InetSocketAddress address) {
        client = new NettyClient(address);
        start();
    }

    private void start() {
        client.open();
    }

    @Override
    public void call(Object msg) {
        client.getChannel().writeAndFlush(msg);
    }
}
