package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.postgres;

import com.cn.xiguaapp.r2dbc.orm.rdb.exception.DuplicateKeyException;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres.PostgresqlSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.BasicReactiveTests;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.BasicTestEntity;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

public class PostgresqlReactiveTests extends BasicReactiveTests {
    @Override
    protected RDBSchemaMetadata getSchema() {
        return new PostgresqlSchemaMetadata("public");
    }

    @Override
    protected Dialect getDialect() {
        return Dialect.POSTGRES;
    }

    @Override
    protected ReactiveSqlExecutor getSqlExecutor() {

        return new TestReactiveSqlExecutor(new PostgresqlR2dbcConnectionProvider());
    }

    @Test
    public void testException() {
        repository.insert(Mono.just(BasicTestEntity.builder()
                .name("test")
                .id("test")
                .state((byte) 1)
                .build()))
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

        repository.insert(Mono.just(BasicTestEntity.builder()
                .name("test")
                .id("test")
                .state((byte) 1)
                .build()))
                .as(StepVerifier::create)
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    @Ignore
    public void benchmark() {
        long time = System.currentTimeMillis();

        StepVerifier
                .create(repository.insertBatch(Flux.range(0, 100000)
                        .map(integer -> BasicTestEntity.builder()
                                .id("id:" + integer)
                                .balance(1000L)
                                .name("test:" + integer)
                                .createTime(new Date())
                                .state((byte) 1)
                                .build())
                        .buffer(1000)))
                .expectNext(100000)
                .verifyComplete();
        System.out.println(System.currentTimeMillis() - time);
    }
}
