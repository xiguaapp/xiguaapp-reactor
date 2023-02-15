package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments.update;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.function.FunctionFragmentBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.update.DefaultUpdateSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperatorParameter;
import com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.MetadataHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DefaultUpdateSqlBuilderTest {
    private DefaultUpdateSqlBuilder builder;

    @Before
    public void init() {
        RDBSchemaMetadata schema = MetadataHelper.createMockSchema();

        builder = DefaultUpdateSqlBuilder.of(schema.getTable("test").orElseThrow(NullPointerException::new));
    }


    @Test
    public void test() {
        UpdateOperatorParameter parameter = new UpdateOperatorParameter();

        {
            UpdateColumn column = new UpdateColumn();
            column.setColumn("name");
            column.setValue("admin");
            parameter.getColumns().add(column);
        }

        {
            parameter.getWhere().addAll(Query.of().where("id", "1234").and().notNull("name").getParam().getTerms());
        }

        SqlRequest request = builder.build(parameter);
        System.out.println(request);
        assertEquals(request.getSql(), "update PUBLIC.test set \"NAME\" = ? where \"ID\" = ? and \"NAME\" is not null");
        assertArrayEquals(request.getParameters(), new Object[]{"admin", "1234"});

    }

    @Test
    public void testFunction() {
        UpdateOperatorParameter parameter = new UpdateOperatorParameter();

        {
            UpdateColumn column = new UpdateColumn();
            column.setColumn("name");
            column.setValue("admin");
            parameter.getColumns().add(column);
        }

        {
            UpdateColumn column = new UpdateColumn();
            column.setFunction("incr");
            column.setOpts(Collections.singletonMap("incr", 1));
            column.setColumn("name");
            column.setValue("admin");
            parameter.getColumns().add(column);
        }

        {
            parameter.getWhere().addAll(Query.of().where("id", "1234").getParam().getTerms());
        }

        builder.getTable()
                .getColumn("name")
                .ifPresent(column -> column.addFeature(new FunctionFragmentBuilder() {
                    @Override
                    public String getFunction() {
                        return "incr";
                    }

                    @Override
                    public SqlFragments create(String columnFullName, RDBColumnMetadata metadata, Map<String, Object> opts) {
                        return PrepareSqlFragments.of().addSql(columnFullName, "+ ?")
                                .addParameter(opts.getOrDefault("incr", 1));
                    }

                    @Override
                    public String getName() {
                        return "自增";
                    }
                }));

        SqlRequest request = builder.build(parameter);
        System.out.println(request);
        assertEquals(request.getSql(), "update PUBLIC.test set \"NAME\" = ? , \"NAME\" = name + ? where \"ID\" = ?");
        assertArrayEquals(request.getParameters(), new Object[]{"admin", 1, "1234"});

    }
}