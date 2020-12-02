package com.xiaofangmoon.springlearn.biz.impl;

import com.xiaofangmoon.springlearn.biz.IBizIndexService;
import com.xiaofangmoon.springlearn.common.domain.model.Person;
import com.xiaofangmoon.springlearn.common.domain.model.Student;
import com.xiaofangmoon.springlearn.core.dao.PersonMapper;
import com.xiaofangmoon.springlearn.core.dao.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiaofang
 */
@Service
public class BizIndexServiceImpl implements IBizIndexService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    PersonMapper personMapper;

    @Override
    @Transactional
    public Object testQuery() {
        Student student = (Student) testQuery1();
        return student;
    }

    @Override
    public Object testQuery1() {

        return studentMapper.selectByPrimaryKey(1);
    }

    @Override
    public Object testInsert() {
        return testInsert1();
    }

    @Override
    @Transactional
    public Object testInsert1() {
        Student student = new Student();
        student.setName("gogo");
        student.setStudyId(0);
        student.setIdCard("1");
        student.setSex("1");
        student.setPhone("18701053876");
        int i = studentMapper.insertSelective(student);

        Integer a = null;
        if (a == null) {
            throw new RuntimeException("ggogoo");
        }
        /*Person person = new Person();
        person.setAge(11);
        person.setName("xiaofang");
        person.setNickName("gogo");
        personMapper.insertSelective(person);*/

        return i;

    }
}
