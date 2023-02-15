package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;


import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.TermSupplier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class UpdateOperator {

    @SafeVarargs
    public final <T> UpdateOperator set(MethodReferenceColumn<T>... columnValues) {
        for (MethodReferenceColumn<T> columnValue : columnValues) {
            this.set(columnValue.getColumn(), columnValue.get());
        }
        return this;
    }

    @SafeVarargs
    public final UpdateOperator set(Supplier<? extends UpdateColumn>... columns) {
        for (Supplier<? extends UpdateColumn> column : columns) {
            set(column.get());
        }
        return this;
    }


    public UpdateOperator set(String column, String sql, Object... parameter) {
        return set(NativeSqlUpdateColumn.of(column, sql, parameter));
    }


    public UpdateOperator set(Map<String, Object> values) {
        values.forEach(this::set);
        return this;
    }

    public abstract UpdateOperator set(String column, Object value);
    public abstract UpdateOperator set(UpdateColumn column);
    public abstract UpdateOperator set(Object entity);

    public abstract UpdateOperator where(Consumer<Conditional<?>> dsl);

    public abstract UpdateOperator where(Term term);

    public final UpdateOperator where(TermSupplier... condition) {
        for (Supplier<Term> operator : condition) {
            where(operator.get());
        }
        return this;
    }

    public UpdateOperator accept(Consumer<UpdateOperator> consumer){
        consumer.accept(this);
        return this;
    }

    public abstract SqlRequest getSql();

    public abstract UpdateResultOperator execute();

}
