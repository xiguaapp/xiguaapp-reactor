package com.cn.xiguaapp.r2dbc.common.core.test;

import com.cn.xiguaapp.r2dbc.common.core.test.entity.TestEntity;
import com.cn.xiguaapp.r2dbc.common.core.test.service.TestEntityService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CrudTests  {

    @Autowired
    private TestEntityService service;


    @Test
    public void test(){

        TestEntity entity = TestEntity.of("test",100);

        Mono.just(entity)
                .as(service::insert)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
        System.out.println(entity.getId());
        Assert.assertNotNull(entity.getId());
    }
}
