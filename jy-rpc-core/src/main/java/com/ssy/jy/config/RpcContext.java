package com.ssy.jy.config;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.RpcClientRuntime;
import com.ssy.jy.runtime.RpcRuntime;
import com.ssy.jy.runtime.RpcServerRuntime;
import com.ssy.jy.spi.SpiLoader;
import com.ssy.jy.spi.SpiMeta;
import com.ssy.jy.stub.JdkProxyStubFactory;
import com.ssy.jy.stub.ServerStub;
import com.ssy.jy.stub.Stub;
import com.ssy.jy.stub.StubFactory;
import com.ssy.jy.tools.FunctionWrapper;
import lombok.Getter;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-05
 */
public class RpcContext {
    private final Map<Class<?>, Supplier<?>> referenceFactory = new HashMap<>();
    private final Map<Class<?>, Supplier<?>> serviceFactory = new HashMap<>();
    @Getter
    private ConfigContext configContext;
    private String configFileName = "jy.yaml";
    private StubFactory stubFactory;

    public RpcContext() {
        init();
    }

    public RpcContext(String configFileName) {
        this.configFileName = configFileName;
        init();
    }

    private void init() {
        this.configContext = ConfigContext.parse(getResourceAsStream(configFileName));
        Optional<StubFactory> load = SpiLoader.load(StubFactory.class, SpiMeta.DEFAULT);
        if (load.isEmpty()) {
            throw new RpcException(String.format("can't not load {} StubFactory by spi.", SpiMeta.DEFAULT));
        }
        this.stubFactory = load.get();
        RuntimeConfig serverConfig = this.configContext.getServer();
        RpcRuntime serverRuntime = new RpcServerRuntime(new InetSocketAddress(serverConfig.getHostName(), serverConfig.getPort()));
        createStub(serverConfig.getStub().getServices(), serverRuntime);
        RuntimeConfig clientConfig = this.configContext.getClient();
        RpcRuntime clientRuntime = new RpcClientRuntime(new InetSocketAddress(clientConfig.getHostName(), clientConfig.getPort()));
        createStub(clientConfig.getStub().getServices(), clientRuntime);
    }

    private void createStub(Map<String, String> service, RpcRuntime runtime) {
        for (Map.Entry<String, String> entry : service.entrySet()) {
            Class<?> interfaceType;
            try {
                interfaceType = Class.forName(entry.getKey());
            } catch (ClassNotFoundException e) {
                throw new RpcException(String.format("interfaceType %s not existed.", entry.getKey()));
            }
            String serviceClassType = entry.getValue();
            if ("proxy".equals(serviceClassType)) {
                referenceFactory.put(interfaceType, FunctionWrapper.singleWrapper(() -> stubFactory.getStub(interfaceType, runtime)));
            } else {
                Class<?> serviceClass;
                try {
                    serviceClass = Class.forName(serviceClassType);
                    Object ref = serviceClass.getConstructors()[0].newInstance();
                    Stub serverStub = new ServerStub(interfaceType, runtime);
                    serverStub.setRef(ref);
                    serviceFactory.put(interfaceType, () -> ref);
                } catch (ClassNotFoundException e) {
                    throw new RpcException(String.format("serviceClass %s not existed.", serviceClassType));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RpcException(String.format("Make sure service %s no args constructors existed.", serviceClassType));
                }
            }
        }
    }

    private InputStream getResourceAsStream(String resourceName) {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(resourceName);
        if (resourceAsStream == null) {
            resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        }
        if (resourceAsStream != null) {
            return resourceAsStream;
        }
        throw new RpcException(String.format("config file is not existed. %s", configFileName));
    }


    @SuppressWarnings("unchecked")
    public <T> T getReference(Class<T> clazz) {
        return (T) referenceFactory.get(clazz).get();
    }

    /**
     * 获取可提供的服务.
     *
     * @param clazz 接口类型
     * @param <T>   接口实现
     * @return T 接口实现
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) serviceFactory.get(clazz).get();
    }
}
