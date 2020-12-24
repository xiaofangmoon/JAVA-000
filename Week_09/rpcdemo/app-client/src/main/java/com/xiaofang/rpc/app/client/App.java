package com.xiaofang.rpc.app.client;

import com.xiaofang.rpc.app.IProductService;
import com.xiaofang.rpc.client.RpcProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xiaofang
 */
public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        RpcProxy rpcProxy = (RpcProxy) context.getBean("rpcProxy");
        IProductService productService = rpcProxy.getInstance(IProductService.class);
        productService.get(1L);
        productService.get(null);
    }
}
