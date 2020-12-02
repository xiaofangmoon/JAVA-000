package com.xiaofangmoon.springlearn.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author xiaofang
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSource {

    String value();
}

@Component
@Aspect
class DataSourceConfig {

    @Before("@annotation(dataSource)")
    public void beforeSwitchDataSource(DataSource dataSource) {
        DynamicDataSource.changeDataSource(dataSource.value());
    }

    @After("@annotation(DataSource)")
    public void afterSwitchDataSource() {
        DynamicDataSource.clearDataSource();
    }
}
