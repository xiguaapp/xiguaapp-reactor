package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder;

import com.cn.xiguaapp.r2dbc.orm.rdb.executor.PrepareSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.DefaultQuerySqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.JoinType;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.BuildParameterQueryOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.Orders;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.JDBCType;

import static com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.Orders.desc;


public class DefaultQuerySqlBuilderTest {


    RDBSchemaMetadata schema;

    @Before
    public void init() {
        RDBDatabaseMetadata database = new RDBDatabaseMetadata(Dialect.H2);
        schema = new RDBSchemaMetadata("DEFAULT");

        database.setCurrentSchema(schema);
        database.addSchema(schema);

        RDBTableMetadata table = new RDBTableMetadata();
        table.setName("test");
        RDBTableMetadata detail = new RDBTableMetadata();
        detail.setName("detail");

        schema.addTable(table);
        schema.addTable(detail);

        {
            RDBColumnMetadata id = new RDBColumnMetadata();
            id.setName("id");
            id.setJdbcType(JDBCType.VARCHAR,String.class);
            id.setLength(32);

            RDBColumnMetadata name = new RDBColumnMetadata();
            name.setName("name");
            name.setJdbcType(JDBCType.VARCHAR,String.class);
            name.setLength(64);

            table.addColumn(id);
            table.addColumn(name);
        }

        {
            RDBColumnMetadata id = new RDBColumnMetadata();
            id.setName("id");
            id.setJdbcType(JDBCType.VARCHAR,String.class);
            id.setLength(32);
            RDBColumnMetadata detailInfo = new RDBColumnMetadata();
            detailInfo.setName("comment");
            detailInfo.setJdbcType(JDBCType.VARCHAR,String.class);
            detailInfo.setLength(64);
            detail.addColumn(id);
            detail.addColumn(detailInfo);
        }

        table.addForeignKey(ForeignKeyBuilder.builder()
                .target("detail")
                .alias("info")
                .autoJoin(true)
                .joinType(JoinType.left)

                .build().addColumn("id","id"));

    }

    @Getter
    @Setter
    public class User{
        private String id;
    }

    @Test
    public void testAutoJoin() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");

        query.select("id", "info.comment")
                .where(dsl -> dsl.is("info.comment", "1234"));

        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(schema);
        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());

        System.out.println(sqlRequest);
        String sql = sqlRequest.getSql();
        Assert.assertTrue(sql.contains("where"));
        Assert.assertTrue(sql.contains("join"));

    }

    @Test
    public void test() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");

        query.select("*")
//                .leftJoin("detail", join -> join.as("info").on("test.id=info.id"))
//                .where(dsl -> dsl.is("name", "1234").is("info.comment", "1234"))
                .orderBy(Orders.count("name").asc(), desc("info.comment"))
//                .forUpdate()
                .paging(0, 10);

        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(schema);

        long time = System.currentTimeMillis();

        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());

        System.out.println(System.currentTimeMillis() - time);

        Assert.assertNotNull(sqlRequest);
        Assert.assertNotNull(sqlRequest.getSql());
        System.out.println(((PrepareSqlRequest) sqlRequest).toNativeSql());

    }
}