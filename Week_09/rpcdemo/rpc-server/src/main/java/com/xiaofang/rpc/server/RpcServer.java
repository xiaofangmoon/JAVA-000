package com.xiaofang.rpc.server;

import com.xiaofang.rpc.common.*;
import com.xiaofang.rpc.register.RpcRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaofang
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RpcServer implements ApplicationContextAware, InitializingBean {

    /**
     * 用于保存所有提供服务的方法, 其中key为类的全路径名, value是所有的实现类
     */
    private final Map<String, Object> serviceBeanMap = new HashMap<>();

    //
    /**
     * rpcRegistry 用于注册相关的地址信息
     */
    private RpcRegistry rpcRegistry;

    /**
     * 提供服务的地址信息 格式为 192.168.158.151:9000 类似
     */
    private String serverAddress;


    /**
     * 在Spring容器启动完成后会执行该方法
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("setApplicationContext");
        Map<String, Object> serviceBean = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBean)) {
            for (Object object : serviceBean.values()) {
                String serviceName = object.getClass().getAnnotation(RpcService.class).value().getName();
                this.serviceBeanMap.put(serviceName, object);
            }
        }
        log.info(" 服务器 : " + serverAddress + " 提供的服务列表 : " + serviceBeanMap);
    }


    /**
     * 初始化完成后执行
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        log.info("afterPropertiesSet");
        //创建服务端的通信对象
        ServerBootstrap server = new ServerBootstrap();
        // 创建异步通信的事件组 用于建立TCP连接的
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 创建异步通信的事件组 用于处理Channel(通道)的I/O事件
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //开始设置server的相关参数
            server.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //沾包粘包
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Constant.MAX_FRAME_LENGTH, Unpooled.wrappedBuffer(Constant.LINE_SPLIT)));
                            ch.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                            ch.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                            ch.pipeline().addLast(new RpcServerHandler(serviceBeanMap));

                        }

                    });
            String host = serverAddress.split(":")[0];
            int port = Integer.parseInt(serverAddress.split(":")[1]);
            ChannelFuture future = server.bind(host, port).sync();
            log.info("服务器启动成功:" + future.channel().localAddress());
            //注册服务
            rpcRegistry.createNode(serverAddress);
            log.info("向zkServer注册服务地址信息");
            future.channel().closeFuture().sync();//等待通信完成
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭socket
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
