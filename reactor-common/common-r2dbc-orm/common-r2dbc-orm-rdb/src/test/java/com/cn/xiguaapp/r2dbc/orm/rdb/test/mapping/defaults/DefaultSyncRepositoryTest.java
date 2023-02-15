package com.cn.xiguaapp.r2dbc.orm.rdb.test.mapping.defaults;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.MappingFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultSyncRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa.JpaEntityTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.wrapper.EntityResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DefaultDatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl.TableDDLResultOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2SchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.mapping.TestEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2ConnectionProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

public class DefaultSyncRepositoryTest {

    private SyncRepository<TestEntity, String> repository;


    @Before
    public void init() {
        RDBDatabaseMetadata databaseMetadata = new RDBDatabaseMetadata(Dialect.H2);

        H2SchemaMetadata h2 = new H2SchemaMetadata("PUBLIC");
        databaseMetadata.setCurrentSchema(h2);
        databaseMetadata.addSchema(h2);
        databaseMetadata.addFeature(new TestSyncSqlExecutor(new H2ConnectionProvider()));

        DefaultDatabaseOperator operator = DefaultDatabaseOperator.of(databaseMetadata);

        TableDDLResultOperator resultOperator = operator.ddl()
                .createOrAlter("entity_test")
                .addColumn("id").primaryKey().varchar(32).commit()
                .addColumn("name").varchar(32).commit()
                .addColumn("state").number(4).commit()
                .addColumn("create_time").alias("createTime").datetime().commit()
                .commit();

        resultOperator.sync();

        JpaEntityTableMetadataParser parser = new JpaEntityTableMetadataParser();
        parser.setDatabaseMetadata(databaseMetadata);
        RDBTableMetadata table = parser.parseTableMetadata(TestEntity.class).orElseThrow(NullPointerException::new);

        h2.addTable(table);

        EntityResultWrapper<TestEntity> wrapper = new EntityResultWrapper<>(TestEntity::new);


        wrapper.setMapping(table.<EntityColumnMapping>getFeature(MappingFeatureType.columnPropertyMapping.createFeatureId(TestEntity.class)).orElseThrow(NullPointerException::new));

        repository = new DefaultSyncRepository<>(
                DefaultDatabaseOperator.of(databaseMetadata), table, TestEntity.class, wrapper);

    }

    @Test
    public void testInsertBatch() {
        List<TestEntity> list = Flux.range(0, 100)
                .map(i -> {
                    TestEntity entity = new TestEntity();
                    entity.setId("test_" + i);
                    entity.setName("test_" + i);
                    if (i < 10) {
                        entity.setState((byte) 1);
                    }
                    entity.setCreateTime(new Date());
                    return entity;
                })
                .collectList()
                .block();

        Assert.assertEquals(100, repository.insertBatch(list));
    }

    @Test
    public void testCrud() {
        TestEntity entity = new TestEntity();
        entity.setId("test");
        entity.setName("test");
        entity.setState((byte) 1);
        entity.setCreateTime(new Date());

        Assert.assertEquals(1, repository.save(entity).getAdded());

        TestEntity inDB = repository.findById("test").orElseThrow(NullPointerException::new);

        Assert.assertEquals(entity, inDB);
        entity.setName("test2");
        Assert.assertEquals(1, repository.save(entity).getUpdated());

        Assert.assertEquals(1, repository.createDelete()
                .where(entity::getId)
                .execute());

    }


}