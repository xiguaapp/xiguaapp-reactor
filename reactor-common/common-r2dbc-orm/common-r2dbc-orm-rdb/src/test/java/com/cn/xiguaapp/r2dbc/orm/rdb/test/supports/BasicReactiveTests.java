package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventListener;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive.ReactiveSyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.MappingFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa.JpaEntityTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.wrapper.EntityResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DefaultDatabaseOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperator;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.map;
import static com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.SortOrder.desc;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record.Record;

/**
 * 响应式写法
 */
@Slf4j
public abstract class BasicReactiveTests {

    protected ReactiveRepository<BasicTestEntity, String> repository;
    protected ReactiveRepository<Record, String> addressRepository;

    protected abstract RDBSchemaMetadata getSchema();

    protected abstract Dialect getDialect();

    protected abstract ReactiveSqlExecutor getSqlExecutor();

    protected RDBDatabaseMetadata getDatabase() {
        RDBDatabaseMetadata metadata = new RDBDatabaseMetadata(getDialect());

        RDBSchemaMetadata schema = getSchema();
        schema.addFeature(new DefaultValueGenerator() {
            @Override
            public String getSortId() {
                return "uuid";
            }

            @Override
            public DefaultValue generate(ObjectMetadata meta) {
                return (RuntimeDefaultValue) () -> UUID.randomUUID().toString().replace("-", "");
            }

            @Override
            public String getName() {
                return "UUID";
            }
        });

        schema.addFeature((EventListener) (type, context) -> System.out.println(type));
        log.debug(schema.toString());

        metadata.setCurrentSchema(schema);
        metadata.addSchema(schema);
        ReactiveSqlExecutor sqlExecutor = getSqlExecutor();

        metadata.addFeature(sqlExecutor);
        metadata.addFeature(ReactiveSyncSqlExecutor.of(sqlExecutor));

        return metadata;
    }

    @After
    public void after() {
        try {
            getSqlExecutor().execute(Mono.just(SqlRequests.of("delete from entity_test_table"))).block();
        } catch (Exception e) {

        }
        try {
            getSqlExecutor().execute(Mono.just(SqlRequests.of("delete from test_address"))).block();
        } catch (Exception e) {

        }
    }

    @Before
    public void init() {
        RDBDatabaseMetadata metadata = getDatabase();
        DatabaseOperator operator = DefaultDatabaseOperator.of(metadata);

        JpaEntityTableMetadataParser parser = new JpaEntityTableMetadataParser();
        parser.setDatabaseMetadata(metadata);
        parser.parseTableMetadata(Address.class)
                .ifPresent(address -> {
                    operator.ddl()
                            .createOrAlter(address)
                            .commit()
                            .reactive()
                            .block();
                });

        RDBTableMetadata table = parser.parseTableMetadata(BasicTestEntity.class).orElseThrow(NullPointerException::new);

        operator.ddl()
                .createOrAlter(table)
                .commit()
                .reactive()
                .block();
        EntityResultWrapper<BasicTestEntity> wrapper = new EntityResultWrapper<>(BasicTestEntity::new);
        wrapper.setMapping(table.<EntityColumnMapping>getFeature(MappingFeatureType.columnPropertyMapping.createFeatureId(BasicTestEntity.class)).orElseThrow(NullPointerException::new));


        repository = new DefaultReactiveRepository<>(operator, table, BasicTestEntity.class, wrapper);
        addressRepository = operator.dml().createReactiveRepository("test_address");

    }

    @Test
    public void testReactivePager() {
        RDBDatabaseMetadata database = getDatabase();
        DatabaseOperator operator = DefaultDatabaseOperator.of(database);
        try {
            operator.ddl()
                    .createOrAlter("test_reactive_pager")
                    .addColumn().name("id").number(32).primaryKey().comment("ID").commit()
                    .addColumn().name("id2").number(32).comment("ID2").commit()
                    .commit()
                    .reactive()
                    .block();

            InsertOperator insert = operator.dml()
                    .insert("test_reactive_pager")
                    .columns("id", "id2");

            for (int i = 0; i < 100; i++) {
                insert.values(String.valueOf(i + 1), null);
            }

            StepVerifier.create(insert.execute().reactive())
                    .expectNext(100)
                    .verifyComplete();

            for (int i = 0; i < 10; i++) {
                StepVerifier.create(operator.dml()
                        .query("test_reactive_pager")
                        .select("id")
                        .paging(i, 10)
                        .fetch(map())
                        .reactive()
                        .log(getClass().getName())
                        .map(map -> map.get("id"))
                        .map(Number.class::cast)
                        .collect(Collectors.summingInt(Number::intValue)))
                        .expectNext((((i * 10) + 1) + ((i + 1) * 10)) * 10 / 2)
                        .verifyComplete();
            }
        } finally {
            try {
                operator.sql().reactive()
                        .execute(Mono.just(SqlRequests.of("drop table " + database.getCurrentSchema().getName() + ".test_reactive_pager")))
                        .block();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testRepositoryInsertBach() {
        //10次insert
        StepVerifier.create(repository.insert(Flux.range(0, 10)
                .map(integer -> BasicTestEntity.builder()
                        .id("test_id_2_" + integer)
                        .balance(1000L)
                        .name("test2:" + integer)
                        .createTime(new Date())
                        .tags(Arrays.asList("a", "b", "c", "d"))
                        .state((byte) 1)
                        .stateEnum(StateEnum.enabled)
                        .build())))
                .expectNext(10)
                .verifyComplete();

        //每30条数据批量insert
        StepVerifier
                .create(repository.insertBatch(Flux.range(0, 100)
                        .map(integer -> BasicTestEntity.builder()
                                .id("test_id_" + integer)
                                .balance(1000L)
                                .name("test:" + integer)
                                .createTime(new Date())
                                .state((byte) 1)
                                .build())
                        .buffer(10).delayElements(Duration.ofMillis(100))))
                .expectNext(100)
                .verifyComplete();

    }


    @Test
    public void testReactiveRepositorySave() {
        BasicTestEntity entity = BasicTestEntity.builder()
                .id("test_id_save")
                .balance(1000L)
                .name("test")
                .createTime(new Date())
                .tags(Arrays.asList("a", "b", "c", "d"))
                .state((byte) 1)
                .addressId("test")
                .stateEnum(StateEnum.enabled)
                .build();

        repository.save(Mono.just(entity))
                .map(SaveResult::getTotal)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

        BasicTestEntity entity2 = BasicTestEntity.builder()
                .id("test_id_save2")
                .balance(1000L)
                .name("test")
                .createTime(new Date())
                .tags(Arrays.asList("a", "b", "c", "d"))
                .state((byte) 1)
                .addressId("test")
                .stateEnum(StateEnum.enabled)
                .build();

        entity.setName("test2");

        repository.createQuery()
                .select("*")
                .where("id", "test_id_save")
                .fetch()
                .map(BasicTestEntity::getName)
                .as(StepVerifier::create)
                .expectNext("test")
                .verifyComplete();

        repository.save(Flux.just(entity2, entity))
                .map(SaveResult::getTotal)
                .as(StepVerifier::create)
                .expectNext(2)
                .verifyComplete();

    }


    @Test
    public void testRepositoryCurd() {
        BasicTestEntity entity = BasicTestEntity.builder()
                .id("test_id")
                .balance(1000L)
                .name("test")
                .tags(Arrays.asList("a", "b", "c", "d"))
                .createTime(new Date())
                .state((byte) 1)
                .addressId("test")
                .build();

        addressRepository.insert(Mono.just(Record.newRecord().putValue("id", "test").putValue("name", "test_address")))
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

        Mono.just(entity)
                .as(repository::insert)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

        Mono.just(entity.getId())
                .as(repository::findById)
                .as(StepVerifier::create)
                .expectNext(entity)
                .verifyComplete();

        repository.createQuery()
                .count()
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

        repository.createQuery()
                .where(entity::getId)
                .orderBy(desc(entity::getId))
                .fetch()
                .as(StepVerifier::create)
                .expectNext(entity)
                .verifyComplete();

        entity.setBalance(100000L);
        repository.createUpdate()
                .set(entity::getBalance)
                .where(entity::getId)
                .execute()
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

        repository.createDelete()
                .where(entity::getId)
                .execute()
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();

    }

}
