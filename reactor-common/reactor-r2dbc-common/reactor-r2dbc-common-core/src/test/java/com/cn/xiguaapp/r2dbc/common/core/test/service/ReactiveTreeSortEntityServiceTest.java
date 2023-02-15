package com.cn.xiguaapp.r2dbc.common.core.test.service;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryParamEntity;
import com.cn.xiguaapp.r2dbc.common.core.test.entity.TestTreeSortEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ReactiveTreeSortEntityServiceTest {

    @Autowired
    private TestTreeSortEntityService sortEntityService;


    @Test
    public void testCrud(){
        TestTreeSortEntity entity=new TestTreeSortEntity();
        entity.setId("test");
        entity.setName("test");

        TestTreeSortEntity entity2=new TestTreeSortEntity();
        entity2.setName("test2");

        entity.setChildren(Arrays.asList(entity2));

        sortEntityService.insert(Mono.just(entity))
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();

        sortEntityService.save(Mono.just(entity))
                .map(SaveResult::getTotal)
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();

        sortEntityService.queryResultToTree(QueryParamEntity.of())
                .map(List::size)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();


        sortEntityService.deleteById(Mono.just("test"))
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();
    }

}