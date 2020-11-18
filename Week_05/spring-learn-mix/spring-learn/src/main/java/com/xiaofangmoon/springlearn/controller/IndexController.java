package com.xiaofangmoon.springlearn.controller;

import com.alibaba.fastjson.JSON;
import com.xiaofangmoon.demo.starter.service.HelloService;
import com.xiaofangmoon.springlearn.entity.Person;
import com.xiaofangmoon.springlearn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xiaofang
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    User user;

    @Autowired
    HelloService helloService;


    @GetMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return JSON.toJSONString(user);
    }

    @GetMapping("/test")
    public String test() {
        return JSON.toJSONString(new Person());
    }

    @GetMapping("/starter")
    public String starter() {
        return JSON.toJSONString(helloService);
    }
}
