package com.xiaofang.rpc.client;

import com.xiaofang.rpc.common.*;
import com.xiaofang.rpc.exception.RpcException;
import com.xiaofang.rpc.register.RpcDiscover;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaofang
 */
@Slf4j
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcResponse rpcResponse;

    private RpcRequest rpcRequest;

    private Object object = new Object();

    private RpcDiscover rpcDiscover;

    public RpcClient(RpcRequest rpcRequest, RpcDiscover rpcDiscover) {
        this.rpcRequest = rpcRequest;
        this.rpcDiscover = rpcDiscover;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) {
        this.rpcResponse = msg;
        log.info("服务器响应消息 ： " + msg);
        synchronized (object) {
            //刷新缓存
            ctx.flush();
            //唤醒等待
            object.notifyAll();
        }
    }

    public RpcResponse send() throws Throwable {
        Bootstrap client = new Bootstrap();

        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        try {
            client.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //加上这个，里面是最大接收、发送的长度
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(Constant.MAX_BUFFER_SIZE))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //沾包粘包
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Constant.MAX_FRAME_LENGTH, Unpooled.wrappedBuffer(Constant.LINE_SPLIT)));
                            ch.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                            ch.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                            ch.pipeline().addLast(RpcClient.this);
                        }
                    })
            ;
            String serverAdderss = rpcDiscover.discover();
            log.info("target : " + serverAdderss);
            String host = serverAdderss.split(":")[0];
            int port = Integer.valueOf(serverAdderss.split(":")[1]);
            ChannelFuture future = client.connect(host, port).sync();
            log.info("客户端准备发送数据 : " + rpcRequest);
            future.channel().writeAndFlush(rpcRequest).sync();
            synchronized (object) {
                object.wait();
            }
            if (rpcResponse != null) {
                future.channel().closeFuture().sync();
            } else {
                throw new RpcException("没有返回值");
            }
            return rpcResponse;

        } finally {
            //优雅关闭socket
            loopGroup.shutdownGracefully();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
