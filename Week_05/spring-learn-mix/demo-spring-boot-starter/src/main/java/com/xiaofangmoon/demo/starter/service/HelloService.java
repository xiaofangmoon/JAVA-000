package com.xiaofangmoon.demo.starter.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaofang
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloService {
    String name;
    String age;
    String hometown;
}
