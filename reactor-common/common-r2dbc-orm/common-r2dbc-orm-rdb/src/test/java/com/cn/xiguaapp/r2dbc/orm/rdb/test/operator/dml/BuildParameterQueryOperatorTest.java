package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.dml;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.BuildParameterQueryOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.Joins;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;
import org.junit.Assert;
import org.junit.Test;

import static com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.Selects.count;

public class BuildParameterQueryOperatorTest {

    @Test
    public void test() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");

        query.select(count("id").as("total"))
                .join(Joins.left("detail").as("info"))
                .where(dsl -> dsl.like("name", "1234"))
                .paging(10, 0);

        QueryOperatorParameter parameter = query.getParameter();

        Assert.assertEquals(parameter.getFrom(), "u_user");
        Assert.assertFalse(parameter.getSelect().isEmpty());
        Assert.assertFalse(parameter.getJoins().isEmpty());
        Assert.assertFalse(parameter.getWhere().isEmpty());
        Assert.assertEquals(parameter.getPageIndex(), Integer.valueOf(10));
        Assert.assertEquals(parameter.getPageSize(), Integer.valueOf(0));
    }

    @Test
    public void testFromParameter() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");

        query.setParam(Query.of()
                .select("id", "total")
                .where("name", "1234")
                .doPaging(10,0)
                .getParam());


        QueryOperatorParameter parameter = query.getParameter();

        Assert.assertEquals(parameter.getFrom(), "u_user");
        Assert.assertFalse(parameter.getSelect().isEmpty());
        Assert.assertFalse(parameter.getWhere().isEmpty());
        Assert.assertEquals(parameter.getPageIndex(), Integer.valueOf(10));
        Assert.assertEquals(parameter.getPageSize(), Integer.valueOf(0));
    }


}