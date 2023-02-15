package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.FunctionColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.Functions;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.SortOrderSupplier;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public interface Orders {

    static OrderOperator function(Supplier<FunctionColumn> column){
        return new OrderOperator(column.get());
    }

    static OrderOperator count(String name){
        return function(Functions.count(name));
    }

    static SortOrderSupplier asc(String column){
        return new OrderOperator(column).asc();
    }

    static SortOrderSupplier desc(String column){
        return new OrderOperator(column).desc();
    }
}
