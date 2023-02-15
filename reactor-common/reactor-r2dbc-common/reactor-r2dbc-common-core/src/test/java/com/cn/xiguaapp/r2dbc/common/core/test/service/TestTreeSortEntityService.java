package com.cn.xiguaapp.r2dbc.common.core.test.service;

import com.cn.xiguapp.common.core.gen.IDGenerator;
import com.cn.xiguaapp.r2dbc.common.core.service.GenericReactiveCrudService;
import com.cn.xiguaapp.r2dbc.common.core.service.ReactiveTreeSortEntityService;
import com.cn.xiguaapp.r2dbc.common.core.test.entity.TestTreeSortEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTreeSortEntityService extends GenericReactiveCrudService<TestTreeSortEntity,String>
        implements ReactiveTreeSortEntityService<TestTreeSortEntity,String> {

    @Override
    public IDGenerator<String> getIDGenerator() {
        return IDGenerator.MD5;
    }

    @Override
    public void setChildren(TestTreeSortEntity entity, List<TestTreeSortEntity> children) {
        entity.setChildren(children);
    }

    @Override
    public List<TestTreeSortEntity> getChildren(TestTreeSortEntity entity) {
        return entity.getChildren();
    }


}
