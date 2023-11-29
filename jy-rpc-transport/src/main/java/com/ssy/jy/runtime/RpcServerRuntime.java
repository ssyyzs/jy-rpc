package com.ssy.jy.runtime;

import com.ssy.jy.transport.NettyClient;
import com.ssy.jy.transport.NettyServer;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-29
 */
public class RpcServerRuntime implements RpcRuntime {
    private NettyServer server;
    private InetSocketAddress address;

    public RpcServerRuntime(InetSocketAddress address) {
        this.address = address;
        this.server = new NettyServer(address);
        start();
    }

    private void start() {
        server.open();
    }

    @Override
    public void call(Object msg) {
        throw new RuntimeException("server runtime is not supported.");
    }
}
