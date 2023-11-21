package com.ssy.jy.transport;

/**
 * @author y30010171
 * @since 2022-08-24
 **/
public interface Serializer {
    Serializer DEFAULT = JSONSerializer.INSTANCE;

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
