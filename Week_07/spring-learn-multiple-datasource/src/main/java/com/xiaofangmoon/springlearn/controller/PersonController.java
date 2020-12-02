package com.xiaofangmoon.springlearn.controller;

import com.alibaba.fastjson.JSON;
import com.xiaofangmoon.demo.starter.service.HelloService;
import com.xiaofangmoon.springlearn.aspect.MyCache;
import com.xiaofangmoon.springlearn.biz.IBizIndexService;
import com.xiaofangmoon.springlearn.core.service.IPersonService;
import com.xiaofangmoon.springlearn.entity.Person;
import com.xiaofangmoon.springlearn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaofang
 */
@RestController
@RequestMapping("/person")
public class PersonController {


    @Autowired
    HelloService helloService;

    @Autowired
    IPersonService personService;


    @GetMapping("/")
    public String index() {

        return "person";
    }

    @GetMapping("/test-db")
    public Object testDb1() {

        return personService.getInfoById(4);
    }

    @GetMapping("/test-db2")
    public Object testDb2() {

        return personService.getInfoById1(4);
    }

}
