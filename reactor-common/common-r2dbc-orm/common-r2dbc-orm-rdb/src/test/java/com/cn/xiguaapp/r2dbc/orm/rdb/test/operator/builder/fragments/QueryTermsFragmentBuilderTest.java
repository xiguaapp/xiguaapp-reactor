package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.builder.fragments;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query.QueryTermsFragmentBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.JDBCType;
import java.util.Collections;
import java.util.List;

public class QueryTermsFragmentBuilderTest {


    public RDBTableMetadata table;

    public QueryTermsFragmentBuilder builder;

    @Before
    public void init() {
        RDBDatabaseMetadata database = new RDBDatabaseMetadata(Dialect.H2);
        RDBSchemaMetadata schema = new RDBSchemaMetadata("DEFAULT");

        database.setCurrentSchema(schema);
        database.addSchema(schema);

        table = new RDBTableMetadata();
        table.setName("test");
        schema.addTable(table);

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

        builder = QueryTermsFragmentBuilder.of(table);
        builder.setUseBlock(true);
    }

    private SqlRequest createSqlRequest(List<Term> terms) {

        QueryOperatorParameter parameter = new QueryOperatorParameter();
        parameter.setFrom("test");
        parameter.setWhere(terms);

        SqlFragments fragments = builder.createFragments(parameter);
        Assert.assertNotNull(fragments.getSql());
        SqlRequest request = fragments.toRequest();
        System.out.println(request.getSql());
        System.out.println(fragments.toString());

        return request;
    }

    public SqlRequest createSqlRequest(Query<Object, QueryParam> query) {
        return createSqlRequest(query.getParam().getTerms());
    }

    public void assertSql(Query<Object, QueryParam> query, String sql) {
        Assert.assertEquals(createSqlRequest(query).getSql(), sql);
    }

    @Test
    public void testComplex() {
        Term term = new Term();

        term.and("id", "1")
                .and("name", "2")
                .nest()
                .and("name", "1234")
                .or("name", "12345")
                .nest()
                .and("name", "1234")
                .or("name", "12345");

        SqlRequest sql = createSqlRequest(Collections.singletonList(term));

//        System.out.println(sql.getSql());
    }

    @Test
    public void testSimple() {
        assertSql(
                Query.of().is("id", "1").is("name", "123"),
                "test.\"ID\" = ? and test.\"NAME\" = ?"
        );
    }

    @Test
    public void testNull() {
        assertSql(
                Query.of().is("id", "1").is("name", null).is("name","123"),
                "test.\"ID\" = ? and test.\"NAME\" = ?"
        );
    }

    @Test
    public void testFullColumnName() {
        assertSql(
                Query.of().is("test.id", "1").is("test.name", "123"),
                "test.\"ID\" = ? and test.\"NAME\" = ?"
        );
    }


    @Test
    public void testNest() {
        assertSql(
                Query.of()
                        .is("id", "1").and()
                        .nest().is("name", "1234")
                        .orNest()
                        .is("id", "1234")
                        .and()
                        .is("id", "123")
                        .end()
                        .end()
                ,
                "test.\"ID\" = ? and ( test.\"NAME\" = ? or ( test.\"ID\" = ? and test.\"ID\" = ? ) )"
        );
    }

}