package com.ssy.jy.transport;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;

/**
 * jy编解码器.
 *      * 前4位为魔数
 *      * 第5位为版本号
 *      * 第6位为序列化算法
 *      * 第7位为命令类型
 *      * 第8-11位为数据长度
 *      * 第12位往后为数据内容.
 *
 * @author ssyyzs
 * @since 2023-11-21
 **/
public class JyCodec {
    public static final int MAGIC_NUMBER = 0x12345678;
    public static final Map<Byte, Class<? extends Packet>> ALL_PACKET_CLASS = new HashMap<>();

    static {
        ALL_PACKET_CLASS.put(Command.REQUEST_COMMAND, RpcRequestPacket.class);
        ALL_PACKET_CLASS.put(Command.REQUEST_RESPONSE_COMMAND, RpcResponsePacket.class);
    }

    /**
     * 将目标Packet编码成ByteBuf.使用自定义编码格式填充ByteBuf
     *
     * @param byteBuf 网络层待传输的字节数组载体
     * @param packet 待传输的报文
     * @return 返回byteBuf
     */
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

    /**
     * 将目标ByteBuf解码成Packet.使用自定义编码格式填充ByteBuf
     *
     * @param byteBuf 网络层已接收的字节数组载体
     * @return 解析后的报文
     */
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
        return ALL_PACKET_CLASS.get(command);
    }

    private static Serializer getSerializer(byte algorithm) {
        if (algorithm == SerializerAlgorithm.JSON) {
            return JsonSerializer.INSTANCE;
        }
        return null;
    }
}
