package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DefaultDatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2SchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2TableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2ConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.mapStream;
import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.singleMap;


public class DefaultDatabaseOperatorTest {

    private RDBDatabaseMetadata database;

    private DatabaseOperator operator;

    @Before
    public void init() {
        database = new RDBDatabaseMetadata(Dialect.H2);

        SyncSqlExecutor sqlExecutor = new TestSyncSqlExecutor(new H2ConnectionProvider());
        database.addFeature(sqlExecutor);

        H2SchemaMetadata schema = new H2SchemaMetadata("PUBLIC");



        schema.addFeature(new H2TableMetadataParser(schema));
        database.addSchema(schema);
        database.setCurrentSchema(schema);


        operator = DefaultDatabaseOperator.of(database);

    }


    @Test
    public void testDDLCreate() {
        operator.ddl()
                .createOrAlter("test_ddl_create")
                .addColumn().name("id").varchar(32).primaryKey().comment("ID").commit()
                .addColumn().name("name").varchar(64).notNull().comment("名称").commit()
                .addColumn().name("comment").columnDef("varchar(32) not null default '1'").commit()
                .index().name("index_").column("name").commit()
                .commit()
                .sync();

        operator.dml()
                .insert("test_ddl_create")
                .value("id", "1234")
                .value("name", "名称")
                .execute()
                .sync();

        int sum = operator.dml()
                .query("test_ddl_create")
                .select("comment")
                .fetch(mapStream())
                .sync()
                .map(map -> map.get("comment"))
                .map(String::valueOf)
                .mapToInt(Integer::valueOf)
                .sum();
        Assert.assertEquals(sum, 1);
    }

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    @NoArgsConstructor
    public static class TestEntity implements Serializable {
        private String id;
        private String name;

        private int status;
    }

    @Test
    public void testDmlCrud() {

        operator.sql()
                .sync()
                .execute(SqlRequests.of("create table test_dm_crud(id varchar(32) primary key,name varchar(64) not null,status number(4) )"));

        TestEntity entity = TestEntity.of("test", "test_name", 1);

        TestEntity entity2 = TestEntity.of("test2", "test2_name", 1);

        int len = operator
                .dml()
                .insert("test_dm_crud")
                .values(entity::getId, entity::getName, entity::getStatus)
                .values(entity2::getId, entity2::getName, entity2::getStatus)
                .execute()
                .sync();
        Assert.assertEquals(len, 2);

        len = operator
                .dml()
                .update("test_dm_crud")
                .set("name", "new_name")
                .where(dsl -> dsl.is("id", "test"))
                .execute()
                .sync();

        Assert.assertEquals(len, 1);


        Map<String, Object> data = operator.dml()
                .query("test_dm_crud")
                .select("id", "name")
                .where(dsl -> dsl.is("id", "test"))
                .fetch(singleMap())
                .sync();
        Assert.assertEquals(data.get("id"), "test");
        Assert.assertEquals(data.get("name"), "new_name");

        len = operator.dml()
                .delete("test_dm_crud")
                .where(dsl -> dsl.is("id", "test"))
                .execute()
                .sync();

        Assert.assertEquals(len, 1);

    }
}