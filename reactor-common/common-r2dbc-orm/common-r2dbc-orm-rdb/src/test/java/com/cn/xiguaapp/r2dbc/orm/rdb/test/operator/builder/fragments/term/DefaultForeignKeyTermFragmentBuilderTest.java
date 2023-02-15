package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments.term;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.term.DefaultForeignKeyTermFragmentBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.MetadataHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class DefaultForeignKeyTermFragmentBuilderTest {


    public DefaultForeignKeyTermFragmentBuilder builder;

    public ForeignKeyMetadata foreignKeyMetadata;

    @Before
    public void init() {


        RDBSchemaMetadata metadata = MetadataHelper.createMockSchema();


        builder = new DefaultForeignKeyTermFragmentBuilder();

        metadata.getTableOrView("test")
                .ifPresent(test -> foreignKeyMetadata =
                        test.addForeignKey(ForeignKeyBuilder.builder()
                                .name("test")
                                .target("detail")
                                .terms(Collections.singletonList(new Term().and("id", "1234").or("id", "12345")))
                                .build()
//                                .addMiddle(ForeignKeyBuilder.builder()
//                                        .name("test2")
//                                        .target("test")
//                                        .source("detail")
//                                        .build().addColumn("comment","id"))
                                .addColumn("id","id")));
    }

    @Test
    public void test() {
        SqlFragments fragments = builder
                .createFragments("test", foreignKeyMetadata,
                        Query.of().where("detail.comment", 1)
                                .getParam().getTerms());

        SqlRequest request = fragments.toRequest();
        System.out.println(fragments.toRequest());
        Assert.assertEquals(request.getSql(), "exists( select 1 from PUBLIC.detail detail where test.\"ID\" = detail.\"ID\" and ( ( detail.\"ID\" = ? or detail.\"ID\" = ? ) and ( detail.\"COMMENT\" = ? ) ) )");

    }

}