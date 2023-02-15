package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.postgres;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres.PostgresqlSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres.PostgresqlTableMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.JDBCType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests.prepare;


public class PostgresqlTableMetaParserTest {

    private SyncSqlExecutor executor;

    private PostgresqlTableMetadataParser parser;

    @Before
    public void init() {
        executor = new TestSyncSqlExecutor(new PostgresqlConnectionProvider());
        PostgresqlSchemaMetadata schema = new PostgresqlSchemaMetadata("public");
        schema.addFeature(executor);
        parser = new PostgresqlTableMetadataParser(schema);
    }

    @Test
    public void testParseAll() {
        executor.execute(SqlRequests.of("CREATE TABLE IF NOT EXISTS test_table2(" +
                "id varchar(32) primary key," +
                "name varchar(128) not null" +
                ")"));
        try {
            List<RDBTableMetadata> table = parser.parseAll();

            Map<String, RDBTableMetadata> mapping = table.stream()
                    .collect(Collectors.toMap(RDBTableMetadata::getName, Function.identity()));

            Assert.assertNotNull(mapping.get("test_table2"));

        }finally {
            executor.execute(prepare("drop table test_table2"));
        }
    }

    @Test
    public void testParse() {
        executor.execute(SqlRequests.of("CREATE TABLE IF NOT EXISTS test_table(" +
                "id varchar(32) primary key," +
                "name varchar(128) not null," +
                "age int4," +
                "json1 json," +
                "json2 jsonb" +
                ")"));
        try {
            RDBTableMetadata metaData = parser.parseByName("test_table").orElseThrow(NullPointerException::new);

            //id
            {
                RDBColumnMetadata column = metaData.getColumn("id").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);

                Assert.assertEquals(column.getDataType(), "varchar(32)");
                Assert.assertEquals(column.getSqlType(), JDBCType.VARCHAR);
                Assert.assertEquals(column.getJavaType(), String.class);
                Assert.assertTrue(column.isNotNull());
                // 这里只解析表结构，而不会解析键信息.
                // Assert.assertTrue(column.isPrimaryKey());
            }

            //name
            {
                RDBColumnMetadata column = metaData.getColumn("name").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);

                Assert.assertEquals(column.getDataType(), "varchar(128)");
                Assert.assertEquals(column.getLength(), 128);
                Assert.assertEquals(column.getSqlType(), JDBCType.VARCHAR);
                Assert.assertEquals(column.getJavaType(), String.class);
                Assert.assertTrue(column.isNotNull());
            }

            //age
            {
                RDBColumnMetadata column = metaData.getColumn("age").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);
                Assert.assertEquals(column.getPrecision(), 32);
                Assert.assertEquals(column.getScale(), 0);
                Assert.assertEquals(column.getDataType(), "int4");
                Assert.assertEquals(column.getSqlType(), JDBCType.INTEGER);
                Assert.assertEquals(column.getJavaType(), Integer.class);
            }
            //json
            {
                RDBColumnMetadata column = metaData.getColumn("json1").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);
                Assert.assertEquals(column.getDataType(), "json");
                Assert.assertEquals(column.getSqlType(), JDBCType.CLOB);
                Assert.assertEquals(column.getJavaType(), String.class);
            }
            //jsonb
            {
                RDBColumnMetadata column = metaData.getColumn("json2").orElseThrow(NullPointerException::new);

                Assert.assertNotNull(column);
                Assert.assertEquals(column.getDataType(), "jsonb");
                Assert.assertEquals(column.getType().getId(), "jsonb");
                Assert.assertEquals(column.getSqlType(), JDBCType.CLOB);
                Assert.assertEquals(column.getJavaType(), String.class);
            }
        } finally {
            executor.execute(prepare("drop table test_table"));
        }

    }


}