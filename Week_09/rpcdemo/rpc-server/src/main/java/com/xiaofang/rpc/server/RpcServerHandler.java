package com.xiaofang.rpc.server;

import com.xiaofang.rpc.app.Product;
import com.xiaofang.rpc.common.RpcRequest;
import com.xiaofang.rpc.common.RpcResponse;
import com.xiaofang.rpc.exception.RpcException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.Delimiters;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiaofang
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler {

    private Map<String, Object> serviceBeanMap;

    public RpcServerHandler(Map<String, Object> serviceBeanMap) {
        this.serviceBeanMap = serviceBeanMap;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {

        log.info("RpcServerHandler.channelRead");
        RpcRequest rpcRequest = (RpcRequest) msg;
        log.info("请求={}", rpcRequest.toString());
        //处理请求
        RpcResponse rpcResponse = handler(rpcRequest);
        log.info("响应 = {}", rpcResponse);
        //告诉客户端,关闭socket连接
        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);

    }

    public RpcResponse handler(RpcRequest rpcRequest) {
        log.info("handlerRequest = {}", rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResponseId(UUID.randomUUID().toString());
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            String className = rpcRequest.getClassName();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] parameters = rpcRequest.getParameters();
            Class<?> clz = Class.forName(className);
            Object serviceBean = serviceBeanMap.get(className);
            if (serviceBean == null) {
                throw new RpcException(className + " 没有找到对应的serviceBean : " + className + " : beanMap" + serviceBeanMap);
            }
            //反射调用方法
            Method method = clz.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serviceBean, parameters);
            rpcResponse.setSuccess(true);
            //设置方法调用的结果
            rpcResponse.setResult(result);

        } catch (Exception e) {
            log.error("异常={}", e);
            //直接抛出异常会报错，不能序列化
            if (e instanceof InvocationTargetException) {
                rpcResponse.setThrowable(((InvocationTargetException) e).getTargetException());
//                rpcResponse.setThrowable(new RpcException(((InvocationTargetException) e).getTargetException().getMessage()));
            } else {
                rpcResponse.setThrowable(new RpcException(e.getMessage()));
            }
            rpcResponse.setSuccess(false);
            rpcResponse.setResult(new Product(1L, "001", "笔记本电脑", BigDecimal.TEN));
        }
        return rpcResponse;
    }
}
