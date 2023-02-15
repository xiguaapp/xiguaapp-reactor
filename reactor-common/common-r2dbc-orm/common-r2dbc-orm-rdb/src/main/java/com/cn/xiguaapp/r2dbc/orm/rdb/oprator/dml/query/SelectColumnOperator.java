package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;



import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.SelectColumnSupplier;

import java.util.Map;

/**
 * @author xiguaapp
 */
public class SelectColumnOperator implements SelectColumnSupplier {
    private SelectColumn column = new SelectColumn();

    public SelectColumnOperator(String name){
        column.setColumn(name);
    }
    public SelectColumnOperator(String name, String function){
        column.setFunction(function);
        column.setColumn(name);
    }

    public SelectColumnOperator(String name, String function, Map<String,Object> opts){
        column.setFunction(function);
        column.setColumn(name);
        column.setOpts(opts);
    }
    @Override
    public SelectColumn get() {
        return column;
    }

    public SelectColumnSupplier as(String alias) {
        column.setAlias(alias);
        return this;
    }
}
