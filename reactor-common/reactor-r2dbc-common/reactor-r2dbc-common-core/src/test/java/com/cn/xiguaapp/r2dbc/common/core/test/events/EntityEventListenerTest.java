package com.cn.xiguaapp.r2dbc.common.core.test.events;

import com.cn.xiguaapp.r2dbc.common.core.test.TestApplication;
import com.cn.xiguaapp.r2dbc.common.core.test.entity.EventTestEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class EntityEventListenerTest {

    @Autowired
    private ReactiveRepository<EventTestEntity, String> reactiveRepository;

    @Autowired
    private TransactionalOperator transactionalOperator;

    @Autowired
    private TestEntityListener listener;

    @Test
    public void test() {
        Mono.just(EventTestEntity.of("test", 1))
                .as(reactiveRepository::insert)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
        Assert.assertEquals(listener.created.getAndSet(0), 1);


    }

    @Test
    public void testInsertBatch() {
        Flux.just(EventTestEntity.of("test2", 1), EventTestEntity.of("test3", 2))
                .as(reactiveRepository::insert)
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();
        Assert.assertEquals(listener.created.getAndSet(0), 2);

        reactiveRepository.createUpdate().set("age",3).where().in("name","test2","test3").execute()
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();

        Assert.assertEquals(listener.modified.getAndSet(0), 2);

        reactiveRepository.createDelete().where().in("name","test2","test3").execute()
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();

        Assert.assertEquals(listener.deleted.getAndSet(0), 2);

        reactiveRepository.save(EventTestEntity.of("test2", 1))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

        Assert.assertEquals(listener.saved.getAndSet(0), 1);

    }

    @Test
    @Ignore
    public void testInsertError() {
        Flux.just(EventTestEntity.of("test2", 1), EventTestEntity.of("test3", 2))
                .as(reactiveRepository::insert)
                .flatMap(i -> Mono.error(new RuntimeException()))
                .as(transactionalOperator::transactional)
                .as(StepVerifier::create)
                .verifyError();

        Assert.assertEquals(listener.created.getAndSet(0), 0);
    }


}