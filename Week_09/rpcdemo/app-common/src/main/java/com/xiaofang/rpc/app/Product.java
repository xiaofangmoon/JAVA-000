package com.xiaofang.rpc.app;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private Long id;
	private String sn;
	private String name;
	private BigDecimal price;
}
