package com.cn.xiguaapp.r2dbc.orm.rdb.test.mapping.defaults;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.DefaultEntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultSyncQuery;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.wrapper.EntityResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DefaultDatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2SchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.mapping.TestEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2ConnectionProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class DefaultSyncQueryTest {

    private DefaultDatabaseOperator operator;

    @Before
    public void init() {
        RDBDatabaseMetadata databaseMetadata = new RDBDatabaseMetadata(Dialect.H2);

        H2SchemaMetadata h2 = new H2SchemaMetadata("PUBLIC");
        databaseMetadata.setCurrentSchema(h2);
        databaseMetadata.addSchema(h2);
        databaseMetadata.addFeature(new TestSyncSqlExecutor(new H2ConnectionProvider()));

        operator = DefaultDatabaseOperator.of(databaseMetadata);

    }

    @Test
    public void test() {
        operator.ddl()
                .createOrAlter("entity_test")
                .addColumn("id").primaryKey().varchar(32).commit()
                .addColumn("name").varchar(32).commit()
                .addColumn("state").number(4).commit()
                .addColumn("create_time").alias("createTime").datetime().commit()
                .commit()
                .sync();

        Date date= new Date();
        operator.dml()
                .insert("entity_test")
                .columns("id", "name", "state", "createTime")
                .values("test", "1234", 1, date)
                .execute()
                .sync();


        TableOrViewMetadata metadata=operator.getMetadata().getTableOrView("entity_test").orElseThrow(NullPointerException::new);
        EntityResultWrapper<TestEntity> wrapper = new EntityResultWrapper<>(TestEntity::new);
        DefaultEntityColumnMapping mapping=new DefaultEntityColumnMapping( metadata, TestEntity.class);
        metadata.addFeature(mapping);

        mapping.addMapping("id","id");
        mapping.addMapping("name","name");
        mapping.addMapping("state","state");
        mapping.addMapping("create_time","createTime");

        wrapper.setMapping(mapping);

        DefaultSyncQuery<TestEntity> query = new DefaultSyncQuery<>(metadata, mapping,operator.dml(), wrapper);

        Assert.assertEquals(1,  query.count());

        TestEntity entity = query.where("id","test").fetchOne().orElseThrow(NullPointerException::new);
        Assert.assertEquals(entity.getId(),"test");
        Assert.assertEquals(entity.getName(),"1234");
        Assert.assertEquals(entity.getState(),(Object)(byte)1);
        Assert.assertEquals(entity.getCreateTime(),date);



    }
}