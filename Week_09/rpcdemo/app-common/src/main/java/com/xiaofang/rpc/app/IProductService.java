package com.xiaofang.rpc.app;

public interface IProductService {
	String save(Product product);

	void deleteById(Long produceId);

	void update(Product product);

	Product get(Long productId);
}
