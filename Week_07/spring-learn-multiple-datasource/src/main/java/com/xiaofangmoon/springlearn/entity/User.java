package com.xiaofangmoon.springlearn.entity;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaofang
 */
@Data
public class User {
    String id;
    String name;
    String sex;
    int age;
    String nickName;


}