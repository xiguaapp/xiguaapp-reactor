package com.cn.xiguaapp.r2dbc.orm.rdb.test.supports.h2;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2IndexMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2SchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.TestSyncSqlExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class H2IndexMetadataParserTest {

    @Test
    public void test() {
        SyncSqlExecutor sqlExecutor = new TestSyncSqlExecutor(new H2ConnectionProvider());
        sqlExecutor.execute(SqlRequests.of(
                "create table test_index_parser(" +
                        "id varchar(32) primary key," +
                        "name varchar(32) not null," +
                        "age number(4)," +
                        "addr varchar(128))"));


        sqlExecutor.execute(SqlRequests.of("create index test_index on test_index_parser (age)"));

        sqlExecutor.execute(SqlRequests.of("create unique index test_index_2 on test_index_parser (name)"));

        sqlExecutor.execute(SqlRequests.of("create index test_index_3 on test_index_parser (name,addr desc)"));

        H2SchemaMetadata schema = new H2SchemaMetadata("PUBLIC");
        schema.addFeature(sqlExecutor);

        H2IndexMetadataParser parser = new H2IndexMetadataParser(schema);

        List<RDBIndexMetadata> index = parser.parseTableIndex("test_index_parser");

        index.forEach(System.out::println);

        Assert.assertNotNull(index);
        Assert.assertEquals(index.size(), 4);

        {
            RDBIndexMetadata id = index.stream()
                    .filter(meta -> meta.getColumns().stream()
                            .anyMatch(indexColumn -> indexColumn.getColumn().equals("id")))
                    .findFirst().orElseThrow(NullPointerException::new);
            Assert.assertTrue(id.isPrimaryKey());
            Assert.assertTrue(id.isUnique());
        }

        {
            RDBIndexMetadata nameAndAddr = index.stream()
                    .filter(meta -> meta.getName().equals("test_index_3"))
                    .findFirst().orElseThrow(NullPointerException::new);
            Assert.assertFalse(nameAndAddr.isPrimaryKey());
            Assert.assertFalse(nameAndAddr.isUnique());

            Assert.assertEquals(nameAndAddr.getColumns().size(),2);

            Assert.assertEquals(nameAndAddr.getColumns().get(0).getSort(), RDBIndexMetadata.IndexSort.asc);
            Assert.assertEquals(nameAndAddr.getColumns().get(1).getSort(), RDBIndexMetadata.IndexSort.desc);

        }


    }

}