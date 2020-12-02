package com.xiaofangmoon.springlearn.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaofang
 */
@Configuration
public class MultiDataSource {
    public static final String MASTER_DATA_SOURCE = "masterDataSource";
    public static final String SLAVE_DATA_SOURCE = "slaveDataSource";
    public static final String SLAVE1_DATA_SOURCE = "slave1DataSource";
    public static final String SLAVE2_DATA_SOURCE = "slave2DataSource";


    @Bean(name = MultiDataSource.MASTER_DATA_SOURCE)
    @ConfigurationProperties(prefix = "datasource.master")
    public DataSource masterDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = MultiDataSource.SLAVE1_DATA_SOURCE)
    @ConfigurationProperties(prefix = "datasource.slave")
    public DataSource slave1DataSource() {
        return new DruidDataSource();
    }

    @Bean(name = MultiDataSource.SLAVE2_DATA_SOURCE)
    @ConfigurationProperties(prefix = "datasource.slave1")
    public DataSource slave2DataSource() {
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(MASTER_DATA_SOURCE, masterDataSource());
        dataSourceMap.put(SLAVE1_DATA_SOURCE, slave1DataSource());
        dataSourceMap.put(SLAVE2_DATA_SOURCE, slave2DataSource());

        dynamicDataSource.setTargetDataSources(dataSourceMap);
        //动态数据源
        DynamicDataSource.MASTER_DATASOURCE_LIST.add(MASTER_DATA_SOURCE);
        DynamicDataSource.SLAVE_DATASOURCE_LIST.add(SLAVE1_DATA_SOURCE);
        DynamicDataSource.SLAVE_DATASOURCE_LIST.add(SLAVE2_DATA_SOURCE);
        return dynamicDataSource;
    }


}
