# 自定义RPC框架

## 框架架构图

![](https://tva1.sinaimg.cn/large/0081Kckwgy1gly37o1974j30za0ofjv8.jpg)

## 模块介绍

- app-client : 客户端
- app-server : 服务提供者
- rpc-server : rpc服务端处理请求
- rpc-client : rpc客户端处理发送请求
- rpc-common : 解码器，请求和响应对象
- rpc-register: 服务注册中心，居于zookeeper实现

> 底层netty来通信 ，
> zookeeper来作为注册中心

## 问题

1. 处理请求沾包粘包问题

- 设置接收和发送的buffer大小

```java
.option(ChannelOption.RCVBUF_ALLOCATOR,new FixedRecvByteBufAllocator(Constant.MAX_BUFFER_SIZE))
```

- 自定义解码规则

```java
//设置分隔符
 ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Constant.MAX_FRAME_LENGTH,Unpooled.wrappedBuffer(Constant.LINE_SPLIT)));
//然后在编码最后加上分隔符
//RpcDecoder.java
// 把数据写入到下一个通道(channel)或者是发往服务端
         byteBuf.writeBytes(bytes);
//分包标志位
         byteBuf.writeBytes(Constant.LINE_SPLIT);

```