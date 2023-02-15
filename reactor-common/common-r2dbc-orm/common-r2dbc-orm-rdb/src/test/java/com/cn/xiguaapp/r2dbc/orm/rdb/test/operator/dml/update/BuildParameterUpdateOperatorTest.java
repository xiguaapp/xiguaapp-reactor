package com.cn.xiguaapp.r2dbc.orm.rdb.test.operator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.BuildParameterUpdateOperator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperatorParameter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildParameterUpdateOperatorTest {

    @Test
    public void test() {
        BuildParameterUpdateOperator update = new BuildParameterUpdateOperator();

        update.set("name", "1234")
                .where(dsl -> dsl.and("id", "1"));

        UpdateOperatorParameter parameter=update.getParameter();

        assertEquals(parameter.getColumns().get(0).getColumn(),"name");
        assertEquals(parameter.getColumns().get(0).getValue(),"1234");

        assertEquals(parameter.getWhere().get(0).getValue(),"1");
        assertEquals(parameter.getWhere().get(0).getColumn(),"id");

    }
}