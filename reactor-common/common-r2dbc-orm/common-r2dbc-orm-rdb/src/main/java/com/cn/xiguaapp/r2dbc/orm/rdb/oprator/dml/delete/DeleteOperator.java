package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete;


import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 * @desc
 */
public abstract class DeleteOperator {
    public abstract DeleteOperator where(Consumer<Conditional<?>> dsl);

    public abstract DeleteOperator where(Term term);

    @SafeVarargs
    public final DeleteOperator where(Supplier<Term>... condition) {
        for (Supplier<Term> operator : condition) {
            where(operator.get());
        }
        return this;
    }

    public abstract SqlRequest getSql();

    public DeleteOperator accept(Consumer<DeleteOperator> consumer){
        consumer.accept(this);
        return this;
    }

    public abstract DeleteResultOperator execute();
}
