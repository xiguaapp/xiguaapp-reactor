package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments;

import com.cn.xiguaapp.r2dbc.orm.param.SqlTerm;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query.JoinFragmentBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.Join;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.MetadataHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class JoinFragmentBuilderTest {

    JoinFragmentBuilder builder;

    @Before
    public void init() {
        RDBSchemaMetadata schema = MetadataHelper.createMockSchema();

        builder = JoinFragmentBuilder.of(schema.getTableOrView("test").orElseThrow(NullPointerException::new));

    }


    @Test
    public void test(){
        QueryOperatorParameter parameter=new QueryOperatorParameter();
        Join join=new Join();
        join.setTarget("detail");
        join.setAlias("_detail");
        join.setTerms(Collections.singletonList(new SqlTerm("_detail.id=test.id")));
        parameter.setJoins(Arrays.asList(join));

        SqlFragments fragments= builder.createFragments(parameter);

        System.out.println(fragments.toRequest().getSql());
    }

    @Test
    public void testTerm(){
        QueryOperatorParameter parameter=new QueryOperatorParameter();
        Join join=new Join();
        join.setTarget("detail");
        join.setAlias("_detail");
        join.setTerms(Collections.singletonList(new SqlTerm("_detail.id=test.id").and("detail.comment","1234")));
        parameter.setJoins(Collections.singletonList(join));

        SqlFragments fragments= builder.createFragments(parameter);

        System.out.println(fragments.toRequest().getSql());
    }

}