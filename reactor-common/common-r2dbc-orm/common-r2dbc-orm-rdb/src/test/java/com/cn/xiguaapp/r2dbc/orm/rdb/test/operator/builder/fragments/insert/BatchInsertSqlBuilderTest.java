package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments.insert;

import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.BatchInsertSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.MetadataHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class BatchInsertSqlBuilderTest {

    private BatchInsertSqlBuilder builder;

    private RDBSchemaMetadata schema;

    @Before
    public void init() {
        schema = MetadataHelper.createMockSchema();

        builder = BatchInsertSqlBuilder.of(schema.getTable("test").orElseThrow(NullPointerException::new));
    }


    @Test
    public void testDefaultValue() {
        schema.getTable("test")
                .flatMap(table->table.getColumn("id"))
        .ifPresent(id->id.setDefaultValue((RuntimeDefaultValue) () -> "runtime_id"));

        InsertOperatorParameter insert = new InsertOperatorParameter();
        {
            insert.getColumns().add(InsertColumn.of("id"));
        }
        insert.getValues().add(Arrays.asList(new Object[]{null}));

        SqlRequest request = builder.build(insert);
        System.out.println(request);
        Assert.assertArrayEquals(request.getParameters(), new Object[]{"runtime_id"});
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
        insert.getValues().add(Arrays.asList("3", "4"));

        SqlRequest request = builder.build(insert);
        Assert.assertArrayEquals(request.getParameters(), new Object[]{"1", "2", "3", "4"});
        System.out.println(request);

    }
}