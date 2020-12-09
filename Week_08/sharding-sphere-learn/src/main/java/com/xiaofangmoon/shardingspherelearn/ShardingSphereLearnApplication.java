package com.xiaofangmoon.shardingspherelearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@SpringBootApplication
public class ShardingSphereLearnApplication {

	@Autowired
	DataSource dataSource;
	public static void main(String[] args) {
		SpringApplication.run(ShardingSphereLearnApplication.class, args);
	}

}
