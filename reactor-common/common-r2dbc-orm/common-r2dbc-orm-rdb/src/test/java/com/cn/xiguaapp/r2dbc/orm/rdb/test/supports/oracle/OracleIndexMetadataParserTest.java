package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.oracle;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleIndexMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OracleIndexMetadataParserTest {

    @Test
    public void test() {

        SyncSqlExecutor sqlExecutor = new TestSyncSqlExecutor(new OracleConnectionProvider());
        try {

            OracleSchemaMetadata schema= new OracleSchemaMetadata("SYSTEM");
            schema.setDatabase(new RDBDatabaseMetadata(Dialect.ORACLE));

            OracleIndexMetadataParser parser =OracleIndexMetadataParser.of(schema);

            schema.addFeature(sqlExecutor);

            sqlExecutor.execute(SqlRequests.of(
                    "create table test_index_parser(" +
                            "id varchar2(32) primary key," +
                            "name varchar2(32) not null," +
                            "age int," +
                            "addr varchar2(128))"));

            sqlExecutor.execute(SqlRequests.of("create index test_index on test_index_parser (age)"));

            sqlExecutor.execute(SqlRequests.of("create unique index test_index_2 on test_index_parser (name)"));

            sqlExecutor.execute(SqlRequests.of("create index test_index_3 on test_index_parser (name,addr desc)"));

            List<RDBIndexMetadata> list = parser.parseTableIndex("test_index_parser");

            Assert.assertEquals(list.size(),4);
            Assert.assertTrue(list.stream().anyMatch(RDBIndexMetadata::isPrimaryKey));

        } finally {
            try {
                sqlExecutor.execute(SqlRequests.of("drop table test_index_parser"));
            } catch (Exception e) {

            }
        }

    }
}