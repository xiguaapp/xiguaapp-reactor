package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.dml.upsert;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DefaultDatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2SchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2ConnectionProvider;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2.H2R2dbcConnectionProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

public class DefaultSaveOrUpdateOperatorTest {


    private DatabaseOperator databaseOperator;

    @Before
    public void init() {

        RDBDatabaseMetadata database = new RDBDatabaseMetadata(Dialect.H2);
        RDBSchemaMetadata schema = new H2SchemaMetadata("PUBLIC");

        database.addFeature(new TestSyncSqlExecutor(new H2ConnectionProvider()));
        database.addFeature(new TestReactiveSqlExecutor(new H2R2dbcConnectionProvider()));

        database.addSchema(schema);
        database.setCurrentSchema(schema);
        databaseOperator = DefaultDatabaseOperator.of(database);

    }


    @Test
    public void testSync() {
        databaseOperator.ddl()
                .createOrAlter("upsert_test")
                .addColumn("id").defaultValueRuntime(() -> UUID.randomUUID().toString().replace("-", "")).primaryKey().varchar(32).commit()
                .addColumn("name").varchar(32).commit()
                .commit()
                .sync();

        SaveResult result = databaseOperator
                .dml()
                .upsert("upsert_test")
                .value("id", "123")
                .value("name", "test")
                .execute()
                .sync();
        Assert.assertEquals(result.getAdded(), 1);


        result = databaseOperator
                .dml()
                .upsert("upsert_test")
                .value("id", "123")
                .value("name", "test")
                .execute()
                .sync();
        Assert.assertEquals(result.getUpdated(), 1);

        result = databaseOperator
                .dml()
                .upsert("upsert_test")
                .columns("id", "name")
                .values("123", "test")
                .values("234", "test2")
                .values("2345", "test3")
                .execute()
                .sync();
        Assert.assertEquals(result.getUpdated(), 1);
        Assert.assertEquals(result.getAdded(), 2);

        result = databaseOperator
                .dml()
                .upsert("upsert_test")
                .columns("name")
                .values("test")
                .values("test2")
                .values("test3")
                .execute()
                .sync();
        Assert.assertEquals(result.getAdded(), 3);

    }

    @Test
    public void testReactive() {
        databaseOperator.ddl()
                .createOrAlter("upsert_test")
                .addColumn("id").defaultValueRuntime(() -> UUID.randomUUID().toString().replace("-", "")).primaryKey().varchar(32).commit()
                .addColumn("name").varchar(32).commit()
                .commit()
                .reactive()
                .as(StepVerifier::create)
                .expectNext(true)
                .verifyComplete();

        databaseOperator
                .dml()
                .upsert("upsert_test")
                .value("id", "123")
                .value("name", "test")
                .execute()
                .reactive()
                .map(SaveResult::getAdded)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();


        databaseOperator
                .dml()
                .upsert("upsert_test")
                .value("id", "123")
                .value("name", "test")
                .execute()
                .reactive()
                .map(SaveResult::getUpdated)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

         databaseOperator
                .dml()
                .upsert("upsert_test")
                .columns("id", "name")
                .values("123", "test")
                .values("234", "test2")
                .values("2345", "test3")
                .execute()
                .reactive()
                .map(SaveResult::getTotal)
                .as(StepVerifier::create)
                .expectNext(3)
                .verifyComplete();

        databaseOperator
                .dml()
                .upsert("upsert_test")
                .columns("name")
                .values("test")
                .values("test2")
                .values("test3")
                .execute()
                .reactive()
                .map(SaveResult::getAdded)
                .as(StepVerifier::create)
                .expectNext(3)
                .verifyComplete();


    }

}