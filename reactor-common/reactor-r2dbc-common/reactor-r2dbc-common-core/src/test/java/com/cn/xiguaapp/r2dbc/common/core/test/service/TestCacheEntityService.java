package com.cn.xiguaapp.r2dbc.common.core.test.service;

import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCacheSupportCrudService;
import com.cn.xiguaapp.r2dbc.common.core.test.entity.TestEntity;
import org.springframework.stereotype.Service;

@Service
public class TestCacheEntityService extends GenericReactiveCacheSupportCrudService<TestEntity,String> {

}
