package app;

import com.xiaofang.rpc.app.IProductService;
import com.xiaofang.rpc.app.Product;
import com.xiaofang.rpc.client.RpcProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

//模拟客户端启动
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class APP {
	@Autowired
	private RpcProxy rpcProxy;

	private IProductService productService;

	@Before
	public void init() {
		productService = rpcProxy.getInstance(IProductService.class);
	}

	@Test
	public void testSave() throws Exception {
		productService.save(new Product(2L, "002", "内衣", BigDecimal.TEN));
	}
}
