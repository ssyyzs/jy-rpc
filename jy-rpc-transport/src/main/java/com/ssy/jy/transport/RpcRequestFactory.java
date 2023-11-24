package com.ssy.jy.transport;

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
    private static final Map<String, RpcRequestPacket> requestMap = new ConcurrentHashMap<>();

    public static RpcRequestPacket getRequest(String requestId) {
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
        requestMap.put(strRequest.getRequestId(), strRequest);
        return strRequest;
    }
}
