package com.xiaofang.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcEncoder extends MessageToByteEncoder {
    private Class genericClass;

    public RpcEncoder(Class genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {

        if (genericClass.isInstance(o)) {
            log.info("encode");
            log.info("message = {}", o);
            //序列化请求消息为字节数组
            byte[] bytes = SerializationUtil.serialize(o);
            log.info("encodeSize = {}", bytes.length);
            // 把数据写入到下一个通道(channel)或者是发往服务端
            byteBuf.writeBytes(bytes);
            //分包标志位
            byteBuf.writeBytes(Constant.LINE_SPLIT);

        }
    }
}
