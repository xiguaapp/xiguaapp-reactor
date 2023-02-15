package com.cn.xiguaapp.r2dbc.orm.rdb.test.executor.reactive.r2dbc;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.r2dbc.R2dbcReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2R2dbcConnectionProvider;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mssql.MSSQLR2dbcConnectionProvider;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mysql.MysqlR2dbcConnectionProvider;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.postgres.PostgresqlR2dbcConnectionProvider;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.lowerCase;
import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.map;


public class R2dbcReactiveSqlExecutorTest {

    @Test
    public void testH2Database() {

        executeTest(new TestReactiveSqlExecutor(new H2R2dbcConnectionProvider()));

    }

    @Test
    public void testPostgreSQL() {


        executeTest(new TestReactiveSqlExecutor(new PostgresqlR2dbcConnectionProvider()));

    }

    @Test
    public void testMysql(){
        executeTest(new TestReactiveSqlExecutor(new MysqlR2dbcConnectionProvider()));
    }

    @Test
    public void testMSSQL() {


        executeTest(new TestReactiveSqlExecutor("@arg", new MSSQLR2dbcConnectionProvider()));

    }


    public void executeTest(R2dbcReactiveSqlExecutor sqlExecutor) {

        try {
            Mono<Void> mono = sqlExecutor.execute(Mono.just(SqlRequests.of("create table test_r2dbc(id varchar(32) primary key)")));

            StepVerifier.create(mono).verifyComplete();

            //插入10条数据
            Flux.range(1, 10)
                    .map(i -> SqlRequests.of("insert into test_r2dbc(id)values(?)", "" + i))
                    .as(sqlExecutor::update)
                    .as(StepVerifier::create)
                    .expectNext(10)
                    .verifyComplete();

            //查询id并合计
            sqlExecutor.select(Mono.just(SqlRequests.of("select id from test_r2dbc")), lowerCase(map()))
                    .map(map -> map.get("id"))
                    .map(String::valueOf)
                    .map(Integer::valueOf)
                    .collect(Collectors.summingInt(Integer::intValue))
                    .as(StepVerifier::create)
                    .expectNext(55)
                    .verifyComplete()
            ;
        } finally {
            sqlExecutor.execute(Mono.just(SqlRequests.of("drop table test_r2dbc"))).block();
        }
    }
}