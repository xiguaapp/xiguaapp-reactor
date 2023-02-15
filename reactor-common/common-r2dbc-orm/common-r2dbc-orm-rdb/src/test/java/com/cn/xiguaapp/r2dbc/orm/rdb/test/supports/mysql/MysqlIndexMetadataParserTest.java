package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.mysql;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql.MysqlIndexMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql.MysqlSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MysqlIndexMetadataParserTest {

    @Test
    public void test() {

        SyncSqlExecutor sqlExecutor = new TestSyncSqlExecutor(new Mysql57ConnectionProvider());
        try {

            MysqlSchemaMetadata schema= new MysqlSchemaMetadata("demo");

            MysqlIndexMetadataParser parser =new MysqlIndexMetadataParser(schema);

            parser.getSchema().addFeature(sqlExecutor);

            sqlExecutor.execute(SqlRequests.of(
                    "create table test_index_parser(" +
                            "id varchar(32) primary key," +
                            "name varchar(32) not null," +
                            "age int," +
                            "addr varchar(128))"));


            sqlExecutor.execute(SqlRequests.of("create index test_index on test_index_parser (age)"));

            sqlExecutor.execute(SqlRequests.of("create unique index test_index_2 on test_index_parser (name)"));

            sqlExecutor.execute(SqlRequests.of("create index test_index_3 on test_index_parser (name,addr desc)"));

            List<RDBIndexMetadata> list = parser.parseTableIndex("test_index_parser");

            Assert.assertEquals(list.size(),4);

        } finally {
            try {
                sqlExecutor.execute(SqlRequests.of("drop table test_index_parser"));
            } catch (Exception e) {

            }
        }

    }

}