package com.xiaofangmoon.springlearn;

import com.xiaofangmoon.springlearn.entity.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.function.Function;

public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-config.xml");

        User user = (User) context.getBean("eric");
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getSex());
        System.out.println(user.getAge());

        String s1 = new String("xiaofang");
        String s2 = new String("gogo");
        String s3 = new String("godddgo");


        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<String>(s1, 1);
        System.out.println("start");
        System.out.println(atomicStampedReference.getStamp());
        atomicStampedReference.set(s1, 2);
        boolean b = atomicStampedReference.compareAndSet(s1, s2, 1, 2);


        System.out.println(b);
//        System.out.println(atomicStampedReference.getReference());


        AtomicMarkableReference<String> atomicMarkableReference = new AtomicMarkableReference<String>(s1, true);
        atomicMarkableReference.set(s1,true);
        boolean b1 = atomicMarkableReference.compareAndSet(s1, s2, true, true);
        System.out.println(b1);
        System.out.println(atomicMarkableReference.getReference());

        List<Integer> list = new ArrayList<>();
        list.stream().map(new Function<Integer, Object>() {
            @Override
            public Object apply(Integer integer) {
                return null;
            }
        });





    }
}
