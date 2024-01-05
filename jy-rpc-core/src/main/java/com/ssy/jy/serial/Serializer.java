package com.ssy.jy.serial;

/**
 * 序列化接口.
 *
 * @author y30010171
 * @since 2023-11-21
 **/
public interface Serializer {
    Serializer DEFAULT = JsonSerializer.INSTANCE;
    byte JSON_ALGORITHM = 1;

    /**
     * 序列化算法标识
     *
     * @return 算法标识.
     */
    byte type();

    /**
     * java 对象转换成二进制字节
     *
     * @param object 待序列化的对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object object);

    /**
     * 二进制字节转换成java 对象
     *
     * @param clazz 对象类型
     * @param bytes 二进制字节数组
     * @return 反序列化后的java对象
     * @param <T> 对象类型
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
