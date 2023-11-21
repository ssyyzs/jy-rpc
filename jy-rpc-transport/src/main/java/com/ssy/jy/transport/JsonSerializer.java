package com.ssy.jy.transport;

import com.alibaba.fastjson.JSON;

/**
 * json序列化.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class JSONSerializer implements Serializer {

    public static final JSONSerializer INSTANCE = new JSONSerializer();

    private JSONSerializer() {
    }

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
