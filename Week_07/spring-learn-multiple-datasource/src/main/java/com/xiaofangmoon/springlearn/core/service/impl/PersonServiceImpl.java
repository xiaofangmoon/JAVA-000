package com.xiaofangmoon.springlearn.core.service.impl;

import com.xiaofangmoon.springlearn.common.domain.model.Person;
import com.xiaofangmoon.springlearn.config.DataSource;
import com.xiaofangmoon.springlearn.config.MultiDataSource;
import com.xiaofangmoon.springlearn.core.dao.PersonMapper;
import com.xiaofangmoon.springlearn.core.service.IPersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaofang
 */
@Service
public class PersonServiceImpl implements IPersonService {
    @Resource
    PersonMapper personMapper;

    @Override
    @DataSource(value = MultiDataSource.MASTER_DATA_SOURCE)
    public Person getInfoById(Integer id) {
        return personMapper.selectByPrimaryKey(id);
    }

    @Override
    @DataSource(value = MultiDataSource.SLAVE_DATA_SOURCE)
    public Person getInfoById1(Integer id) {
        return personMapper.selectByPrimaryKey(id);
    }
}
