package com.xiaofangmoon.springlearn.core.service;

import com.xiaofangmoon.springlearn.common.domain.model.Person;

/**
 * @author xiaofang
 */
public interface IPersonService {


    Person getInfoById(Integer id);
    Person getInfoById1(Integer id);
}
