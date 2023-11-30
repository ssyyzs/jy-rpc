package com.ssy.jy.runtime.transport;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.JyFuture;

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
public class RpcRequestFactory {
    private static final Map<String, JyFuture> requestMap = new ConcurrentHashMap<>();

    public static void register(JyFuture future) {
        requestMap.put(future.getRequestId(), future);
    }

    public static JyFuture getJyFuture(String requestId) {
        return requestMap.get(requestId);
    }

    /**
     * 获取一个新的rpc请求，并记录.
     *
     * @param method 待调用的方法
     * @param args 方法参数
     * @return 构建后的请求
     */
    public static RpcRequestPacket newRpcRequest(Method method, Object[] args) {
        RpcRequestPacket strRequest = new RpcRequestPacket();
        strRequest.setRequestId(String.valueOf(UUID.randomUUID()));
        strRequest.setMethod(method.getName());
        strRequest.setArgumentsType(method.getParameterTypes());
        strRequest.setArguments(args);
        strRequest.setInterfaceType(method.getDeclaringClass().getName());
        return strRequest;
    }

    public static void success(String requestId, Object result) {
        JyFuture jyFuture = requestMap.get(requestId);
        if (jyFuture == null) {
            throw new RpcException("invalid request " + requestId);
        }
        jyFuture.success(result);
    }
}
