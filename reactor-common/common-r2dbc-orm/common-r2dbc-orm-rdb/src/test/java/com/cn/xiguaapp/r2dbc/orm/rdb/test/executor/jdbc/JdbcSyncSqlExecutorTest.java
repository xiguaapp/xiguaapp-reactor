package com.cn.xiguaapp.r2dbc.orm.rdb.test.executor.jdbc;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.DefaultBatchSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2ConnectionProvider;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests.prepare;
import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.singleMap;

public class JdbcSyncSqlExecutorTest {


    private SyncSqlExecutor executor;

    @SneakyThrows
    @Before
    public void init() {
        executor = new TestSyncSqlExecutor(new H2ConnectionProvider());
    }

    @Test
    public void testBatch() {
        {
            DefaultBatchSqlRequest batch = new DefaultBatchSqlRequest();


            batch.addBatch(SqlRequests.of("create table test_batch( id varchar(32) )"));
            batch.addBatch(SqlRequests.of("COMMENT on table test_batch  is 'test'"));

            executor.execute(batch);

        }

        {
            DefaultBatchSqlRequest batch = new DefaultBatchSqlRequest();


            batch.addBatch(SqlRequests.of("insert into test_batch (id) values(?)", "1"));
            batch.addBatch(SqlRequests.of("insert into test_batch (id) values(?)", "2"));
            batch.addBatch(SqlRequests.of("update test_batch set id = ? where id = ?", "3", "2"));

            Assert.assertEquals(executor.update(batch),3);

            int sum = executor.select(SqlRequests.of("select id from test_batch"), ResultWrappers.mapStream())
                    .map(map -> map.get("ID"))
                    .map(String::valueOf)
                    .mapToInt(Integer::valueOf)
                    .sum();
            Assert.assertEquals(sum,4);

        }

    }

    @SneakyThrows
    @Test
    public void testSelectSingle() {
        Map<String, Object> data = executor.select(prepare("select 1 as data"), singleMap());
        Assert.assertNotNull(data);
        Assert.assertEquals(data.get("DATA"), 1);
    }

    @SneakyThrows
    @Test
    public void testSelectPrepare() {
        Map<String, Object> data = executor.select(prepare("select 1 as data where 1 = ?", 1), singleMap());
        Assert.assertNotNull(data);
        Assert.assertEquals(data.get("DATA"), 1);
    }

    @SneakyThrows
    @Test
    public void testSelectTemplate() {
        Map<String, Object> data = executor.select(SqlRequests.template("select 1 as data where 1 = #{id}", Collections.singletonMap("id", 1)), singleMap());
        Assert.assertNotNull(data);
        Assert.assertEquals(data.get("DATA"), 1);
    }

}