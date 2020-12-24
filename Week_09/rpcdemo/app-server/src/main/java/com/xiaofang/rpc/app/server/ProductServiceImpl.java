package com.xiaofang.rpc.app.server;

import com.xiaofang.rpc.app.IProductService;
import com.xiaofang.rpc.app.Product;
import com.xiaofang.rpc.server.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author xiaofang
 */
@Component
@RpcService(IProductService.class)
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Override
    public String save(Product product) {
        log.info("产品保存成功 ： {}", product);
        return "保存成功";
    }

    @Override
    public void deleteById(Long produceId) {

        log.info("产品删除成功 ： {}", produceId);
    }

    @Override
    public void update(Product product) {
        log.info("产品保存成功 ： {}", product);
    }

    @Override
    public Product get(Long productId) {
        log.info("产品获取成功 ：");
        if (productId == null) {
            throw new RuntimeException("productId空");
        }
        return new Product(1L, "001", "笔记本电脑", BigDecimal.TEN);
    }


}
