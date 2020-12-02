package com.xiaofangmoon.springlearn;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.lang.StringBuilder;

public class JavaAgentTest {

    public void say() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("goggo");
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        System.out.println("xiaofang");
        Date date = new Date();



        System.out.println(date.toString());
        JavaAgentTest javaAgentTest = new JavaAgentTest();
        javaAgentTest.say();


    }
}
