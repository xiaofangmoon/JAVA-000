package com.xiaofang.rpc.app.server;

import com.xiaofang.rpc.server.RpcServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xiaofang
 */
public class BootAppServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");

    }
}
