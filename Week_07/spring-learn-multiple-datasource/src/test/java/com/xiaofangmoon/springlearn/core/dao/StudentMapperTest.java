package com.xiaofangmoon.springlearn.core.dao;

import com.alibaba.fastjson.JSON;
import com.xiaofangmoon.springlearn.SpringLearnApplicationTests;
import com.xiaofangmoon.springlearn.common.domain.model.Student;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;


class StudentMapperTest extends SpringLearnApplicationTests {

    @Resource
    StudentMapper studentMapper;

    @Test
    void deleteByPrimaryKey() {
    }

    @Test
    void insert() {
    }

    @Test
    void insertSelective() {
    }

    @Test
    void selectByPrimaryKey() {

        Student student = studentMapper.selectByPrimaryKey(1);
        System.out.println(JSON.toJSONString(student));
    }

    @Test
    void updateByPrimaryKeySelective() {
    }

    @Test
    void updateByPrimaryKey() {
    }
}