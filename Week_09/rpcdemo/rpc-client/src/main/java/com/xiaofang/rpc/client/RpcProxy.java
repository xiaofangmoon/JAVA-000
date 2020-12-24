package com.xiaofang.rpc.client;

import com.xiaofang.rpc.common.RpcRequest;
import com.xiaofang.rpc.common.RpcResponse;
import com.xiaofang.rpc.exception.RpcException;
import com.xiaofang.rpc.register.RpcDiscover;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 动态代理类,用于获取到每个类的代理对象
 * 对于被代理对象的所有的方法调用都会执行invoke方法
 *
 * @author xiaofang
 */
@Getter
@Setter
public class RpcProxy {
    private RpcDiscover rpcDiscover;

    public <T> T getInstance(Class<T> interfaceClass) {
        T instance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, (proxy, method, args) -> {
            // 创建请求对象
            RpcRequest rpcRequest = new RpcRequest();
            String className = method.getDeclaringClass().getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            rpcRequest.setRequestId(UUID.randomUUID().toString());
            rpcRequest.setClassName(className);
            rpcRequest.setParameterTypes(parameterTypes);
            rpcRequest.setParameters(args);
            rpcRequest.setMethodName(method.getName());
            RpcResponse rpcResponse = new RpcClient(rpcRequest, rpcDiscover).send();
            if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess() == false) {
                //判断抛出异常
                throw new RpcException(rpcResponse.getThrowable().getMessage());
            }
            return rpcResponse.getResult();
        });
        return instance;
    }
}
