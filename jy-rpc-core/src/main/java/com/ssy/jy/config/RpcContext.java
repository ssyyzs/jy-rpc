package com.ssy.jy.config;

import com.ssy.jy.exception.RpcException;
import com.ssy.jy.runtime.RpcClientRuntime;
import com.ssy.jy.runtime.RpcRuntime;
import com.ssy.jy.runtime.RpcServerRuntime;
import com.ssy.jy.stub.JdkProxyStubFactory;
import com.ssy.jy.stub.ProxyStub;
import com.ssy.jy.stub.ServerStub;
import com.ssy.jy.stub.Stub;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-05
 */
public class RpcContext {
    private Map<String, Object> referenceSub = new HashMap<>();
    private Map<String, Object> serviceSub = new HashMap<>();
    @Getter
    private ConfigContext configContext;
    private String configFileName = "jy.yaml";

    public RpcContext() {
        init();
    }

    public RpcContext(String configFileName) {
        this.configFileName = configFileName;
        init();
    }

    private void init() {
        this.configContext = ConfigContext.parse(getResourceAsStream(configFileName));
        RuntimeConfig serverConfig = this.configContext.getServer();
        RpcServerRuntime serverRuntime = new RpcServerRuntime(new InetSocketAddress(serverConfig.getHostName(), serverConfig.getPort()));
        createStub(serverConfig.getStub().getServices(), serverRuntime);
        RuntimeConfig clientConfig = this.configContext.getClient();
        RpcClientRuntime clientRuntime = new RpcClientRuntime(new InetSocketAddress(clientConfig.getHostName(), clientConfig.getPort()));
        createStub(clientConfig.getStub().getServices(), clientRuntime);
    }

    private void createStub(Map<String, String> service, RpcRuntime runtime) {
        try {
            for (Map.Entry<String, String> entry : service.entrySet()) {
                Class interfaceType = Class.forName(entry.getKey());
                String implType = entry.getValue();
                if ("proxy".equals(implType)) {
                    referenceSub.put(entry.getKey(), JdkProxyStubFactory.DEFAULT_FACTORY.getStub(interfaceType, runtime));
                } else {
                    Class implClassType = Class.forName(implType);
                    Object ref = implClassType.getConstructors()[0].newInstance();
                    Stub serverStub = new ServerStub(interfaceType, runtime);
                    serverStub.setRef(ref);
                    serviceSub.put(entry.getKey(), ref);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
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

    public <T> T getReference(Class<T> clazz) {
        return (T) referenceSub.get(clazz.getName());
    }

    public <T> T getService(Class<T> clazz) {
        return (T) serviceSub.get(clazz.getName());
    }
}
