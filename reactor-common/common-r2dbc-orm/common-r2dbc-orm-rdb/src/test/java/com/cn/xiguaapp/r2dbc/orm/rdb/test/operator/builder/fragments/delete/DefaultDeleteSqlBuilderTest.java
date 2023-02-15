package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments.delete;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.delete.DefaultDeleteSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.MetadataHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultDeleteSqlBuilderTest {

    DefaultDeleteSqlBuilder builder;

    @Before
    public void init() {
        RDBSchemaMetadata schema = MetadataHelper.createMockSchema();

        builder = DefaultDeleteSqlBuilder.of(schema.getTable("test").orElseThrow(NullPointerException::new));
    }

    @Test
    public void test(){
        DeleteOperatorParameter parameter=new DeleteOperatorParameter();

        {
            parameter.getWhere().addAll(Query.of().where("id", "1234").and().notNull("name").getParam().getTerms());
        }

        SqlRequest sql= builder.build(parameter);
        System.out.println(sql);
        Assert.assertEquals(sql.getSql(),"delete from PUBLIC.test where \"ID\" = ? and \"NAME\" is not null");

    }
}