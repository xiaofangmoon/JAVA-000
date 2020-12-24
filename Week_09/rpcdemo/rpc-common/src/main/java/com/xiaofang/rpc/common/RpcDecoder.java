package com.xiaofang.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiaofang
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        int size = byteBuf.readableBytes();
        log.info("decodeSize = {}", size);
        if (size < 4) {
            return;
        }
        byte[] bytes = new byte[size];
        //把传递的字节数组读取到bytes中
        byteBuf.readBytes(bytes);
        // 反序列化为对象(RPCRequest/RPCResponse对象)
        Object object = SerializationUtil.deserialize(bytes, genericClass);
        list.add(object);
        //刷新缓存
        ctx.flush();

    }
}
