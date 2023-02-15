package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments.insert;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.BatchSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.MultiInsertSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.MetadataHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class MultiInsertSqlBuilderTest {

    private MultiInsertSqlBuilder builder;

    @Before
    public void init() {
        RDBSchemaMetadata schema = MetadataHelper.createMockSchema();

        builder = MultiInsertSqlBuilder.of(schema.getTable("test").orElseThrow(NullPointerException::new));
    }

    @Test
    public void test() {
        InsertOperatorParameter insert = new InsertOperatorParameter();
        {
            InsertColumn column = new InsertColumn();
            column.setColumn("id");
            insert.getColumns().add(column);
        }
        {
            InsertColumn column = new InsertColumn();
            column.setColumn("name");
            insert.getColumns().add(column);
        }

        insert.getValues().add(Arrays.asList("1", "2"));
        insert.getValues().add(Arrays.asList("3", NativeSql.of("1+1","4")));

        SqlRequest request = builder.build(insert);
        System.out.println(request);
        Assert.assertTrue(request instanceof BatchSqlRequest);

        BatchSqlRequest batch = ((BatchSqlRequest) request);
        Assert.assertArrayEquals(batch.getParameters(),new Object[]{"1","2"});
        Assert.assertArrayEquals(batch.getBatch().get(0).getParameters(),new Object[]{"3","4"});

    }
}