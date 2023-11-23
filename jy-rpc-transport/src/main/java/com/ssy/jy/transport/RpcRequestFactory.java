package com.ssy.jy.transport;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcRequestFactory {
    private static final Map<String, RpcRequestPacket> requestMap = new ConcurrentHashMap<>();

    public static RpcRequestPacket getRequest(String requestId) {
        return requestMap.get(requestId);
    }

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
