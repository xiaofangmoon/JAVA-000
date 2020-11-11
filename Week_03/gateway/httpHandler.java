package gateway;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;


public class httpHandler extends ChannelInboundHandlerAdapter {
    int port;
    String host;
    ChannelHandlerContext originCtx;
    FullHttpRequest req;

    public httpHandler(int port, String host, ChannelHandlerContext ctx, FullHttpRequest req) {
        this.port = port;
        this.host = host;
        this.originCtx = ctx;
        this.req = req;
    }

    public void requestContent() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, req.uri());
                                    httpRequest.headers().add("nio", "xiaofang_moon");
                                    ctx.writeAndFlush(httpRequest);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    FullHttpResponse httpResponse = (FullHttpResponse) msg;

                                    HashMap<String, String> headerMap = new HashMap<>();
                                    httpResponse.headers().forEach(stringStringEntry -> headerMap.put(stringStringEntry.getKey(), stringStringEntry.getValue()));

                                    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                    //添加响应正文
                                    response.content().writeBytes(Unpooled.wrappedBuffer(httpResponse.content()));
                                    //修改响应头
                                    response.headers().set("Content-Length", response.content().readableBytes());
                                    response.headers().set("moon", "gogo");

                                    //发送 HttpResponse
                                    originCtx.writeAndFlush(response);
                                    ReferenceCountUtil.release(msg);
                                    originCtx.close();
                                }
                            });
                        }
                    })
            ;
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
