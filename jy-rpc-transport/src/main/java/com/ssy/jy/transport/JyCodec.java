package com.ssy.jy.transport;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class JyCodec {
    public static final int MAGIC_NUMBER = 0x12345678;
    public static final Map<Byte, Class<? extends Packet>> allPacketClass = new HashMap<>();

    static {
        allPacketClass.put(Command.REQUEST_COMMAND, RequestPacket.class);
        allPacketClass.put(Command.REQUEST_RESPONSE_COMMAND, ResponsePacket.class);
    }

    public static ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.command());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public static Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(4);
        byteBuf.skipBytes(1);
        byte serializeAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        throw new RuntimeException("Decode error, can't find requestType or serializer.");
    }

    private static Class<? extends Packet> getRequestType(byte command) {
        return allPacketClass.get(command);
    }

    private static Serializer getSerializer(byte algorithm) {
        if (algorithm == SerializerAlgorithm.JSON) {
            return JSONSerializer.INSTANCE;
        }
        return null;
    }
}
