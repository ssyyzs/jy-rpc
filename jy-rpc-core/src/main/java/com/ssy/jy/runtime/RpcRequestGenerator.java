package com.ssy.jy.runtime;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.transport.RpcRequestPacket;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rpc请求构建工厂，通过此工厂获取的请求会被记录.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcRequestGenerator {
    private static final Map<String, JyFuture> requestMap = new ConcurrentHashMap<>();

    public static JyFuture register(JyFuture future) {
        return requestMap.putIfAbsent(future.getRequestId(), future);
    }

    public static JyFuture getJyFuture(String requestId) {
        return requestMap.get(requestId);
    }

    /**
     * 获取一个新的rpc请求，并记录.
     *
     * @param method 待调用的方法
     * @param args   方法参数
     * @return 构建后的请求
     */
    public static JyFuture newRpcRequest(Method method, Object[] args) {
        RpcRequestPacket request = new RpcRequestPacket();
        request.setRequestId(String.valueOf(UUID.randomUUID()));
        request.setMethod(method.getName());
        request.setArgumentsType(method.getParameterTypes());
        request.setArguments(args);
        request.setInterfaceType(method.getDeclaringClass().getName());
        JyFuture future = new JyFuture(request.getRequestId(), request);
        register(future);
        return future;
    }

    public static void success(String requestId, Object result) {
        JyFuture jyFuture = requestMap.get(requestId);
        if (jyFuture == null) {
            throw new RpcException("not existed request " + requestId);
        }
        jyFuture.success(result);
    }

    public static void failed(String requestId, String msg) {
        JyFuture jyFuture = requestMap.get(requestId);
        if (jyFuture == null) {
            throw new RpcException("not existed request " + requestId);
        }
        jyFuture.failed(msg);
    }
}
