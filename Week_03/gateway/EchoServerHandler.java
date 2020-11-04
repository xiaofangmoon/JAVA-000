package gateway;

import cn.hutool.http.HttpRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest req = (FullHttpRequest) msg;
        String uri = req.uri();
        System.out.println("server received : " + uri);
        HashMap<String, String> headerMap = new HashMap<>();
        req.headers().forEach(stringStringEntry -> headerMap.put(stringStringEntry.getKey(), stringStringEntry.getValue()));
        String body = HttpRequest.get("http://127.0.0.1:8088" + uri)
                .addHeaders(headerMap)
                .header("nio", "fangjunhong")
                .execute()
                .body();

        ctx.writeAndFlush(Unpooled.copiedBuffer(body, CharsetUtil.UTF_8));

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //添加响应正文
        response.content().writeBytes(Unpooled.wrappedBuffer(body.getBytes()));
        //修改响应头
        response.headers().set("Content-Type", "text/plain;charset=utf-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        response.headers().set("Connection", HttpHeaderValues.KEEP_ALIVE);

        //发送 HttpResponse
        ctx.writeAndFlush(response);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("gogoo");
        cause.printStackTrace();
        ctx.close();
    }
}
