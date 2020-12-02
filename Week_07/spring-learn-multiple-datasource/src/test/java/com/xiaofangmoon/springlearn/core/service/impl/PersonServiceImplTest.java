package com.xiaofangmoon.springlearn.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaofangmoon.springlearn.SpringLearnApplicationTests;
import com.xiaofangmoon.springlearn.common.domain.model.Person;
import com.xiaofangmoon.springlearn.core.service.IPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class PersonServiceImplTest extends SpringLearnApplicationTests {

    @Autowired
    IPersonService personService;

    @Test
    void getInfoById() {
        Person person = personService.getInfoById(4);
        System.out.println(JSON.toJSONString(person));
    }

    @Test
    void getInfoById1() {
        Person person = personService.getInfoById1(4);
        System.out.println(JSON.toJSONString(person));
    }
}