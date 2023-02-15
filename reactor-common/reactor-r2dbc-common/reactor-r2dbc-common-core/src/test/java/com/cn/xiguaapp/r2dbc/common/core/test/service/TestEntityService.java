package com.cn.xiguaapp.r2dbc.common.core.test.service;

import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCrudService;
import com.cn.xiguaapp.r2dbc.common.core.test.entity.TestEntity;
import org.springframework.stereotype.Service;

@Service
public class TestEntityService extends GenericReactiveCrudService<TestEntity,String> {

}
