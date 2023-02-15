package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.FunctionColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.SortOrderSupplier;

/**
 * @author xiguaapp
 */
public class OrderOperator implements SortOrderSupplier {

    private SortOrder order = new SortOrder();

    public OrderOperator(String column){
        order.setColumn(column);
    }

    public OrderOperator(FunctionColumn functionColumn){
        order.setColumn(functionColumn.getColumn());
        order.setFunction(functionColumn.getFunction());
        order.setOpts(functionColumn.getOpts());
    }

    public SortOrderSupplier asc() {
        order.setOrder(SortOrder.Order.asc);
        return this;
    }

    public SortOrderSupplier desc() {
        order.setOrder(SortOrder.Order.desc);
        return this;
    }

    @Override
    public SortOrder get() {
        return order;
    }
}
