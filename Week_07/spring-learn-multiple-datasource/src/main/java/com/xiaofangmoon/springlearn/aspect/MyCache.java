package com.xiaofangmoon.springlearn.aspect;

import java.lang.annotation.*;

/**
 * @author xiaofang
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyCache {
    int value() default 0;
}
