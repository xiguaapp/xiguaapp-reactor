package com.cn.xiguaapp.r2dbc.orm.rdb.test.executor.jdbc;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.jdbc.JdbcReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestJdbcReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2ConnectionProvider;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests.prepare;
import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.map;


public class JdbcReactiveSqlExecutorTest {

    JdbcReactiveSqlExecutor sqlExecutor;

    @Before
    @SneakyThrows
    public void init() {
        sqlExecutor = new TestJdbcReactiveSqlExecutor(new H2ConnectionProvider());
    }

    @Test
    public void testExecute() {

        Mono<Void> ddl = sqlExecutor.execute(Mono.just(SqlRequests.of("create table test( id varchar(32) primary key )")))
                .doOnError(Throwable::printStackTrace);

        StepVerifier.create(ddl)
                .verifyComplete();

        Mono<Integer> counter = sqlExecutor
                .update(Flux.range(0, 10)
                        .doOnNext(i -> System.out.println())
                        .map(num -> prepare("insert into test (id) values (?) ", num)))
                .doOnError(Throwable::printStackTrace);

        StepVerifier.create(counter)
                .expectNext(10)
                .verifyComplete();

        Mono<Long> data = sqlExecutor.select(Mono.just(SqlRequests.of("select * from test")), map())
                .doOnError(Throwable::printStackTrace)
                .map(map -> map.get("ID"))
                .count();

        StepVerifier.create(data)
                .expectNext(10L)
                .verifyComplete();

        Mono<Long> count = sqlExecutor.select(Flux.range(0, 10)
                .map(String::valueOf)
                .map(num -> SqlRequests.of("select * from test where id = ?", num)), map())
                .doOnError(Throwable::printStackTrace)
                .map(map -> map.get("ID"))
                .count();

        StepVerifier.create(count)
                .expectNext(10L)
                .verifyComplete();
    }

}