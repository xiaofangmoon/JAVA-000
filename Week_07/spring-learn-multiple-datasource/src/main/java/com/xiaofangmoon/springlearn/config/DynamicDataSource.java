package com.xiaofangmoon.springlearn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lazycece
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    public static final List<String> MASTER_DATASOURCE_LIST = new ArrayList<>(4);
    public static final List<String> SLAVE_DATASOURCE_LIST = new ArrayList<>(16);
    private static AtomicInteger count = new AtomicInteger(0);


    private static final ThreadLocal<String> DATA_SOURCE_KEY = new ThreadLocal<>();
    private static final int MAX_COUNT = 100;

    static void changeDataSource(String dataSourceKey) {

        //动态数据源切换
        if (Objects.equals(dataSourceKey, MultiDataSource.MASTER_DATA_SOURCE)) {
            DATA_SOURCE_KEY.set(MultiDataSource.MASTER_DATA_SOURCE);
        } else if (Objects.equals(dataSourceKey, MultiDataSource.SLAVE_DATA_SOURCE)) {
            DATA_SOURCE_KEY.set(SLAVE_DATASOURCE_LIST.get(count.get() % SLAVE_DATASOURCE_LIST.size()));
            int i = count.incrementAndGet();
            if (i > MAX_COUNT) {
                count = new AtomicInteger(0);
            }
        }

    }

    static void clearDataSource() {
        DATA_SOURCE_KEY.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DATA_SOURCE_KEY.get();
        LOGGER.info("current data-source is {}", key);
        return key;
    }
}
