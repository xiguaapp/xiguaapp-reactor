package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.Join;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.JoinType;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public class JoinOperator implements Supplier<Join> {

    private Join join = new Join();

    public JoinOperator(String target, JoinType type) {
        join.setTarget(target);
        join.setType(type);
    }

    public final JoinOperator as(String alias) {
        join.setAlias(alias);

        return this;
    }

    public final JoinOperator on(String sql) {
        return on(Wheres.sql(sql));
    }

    public final JoinOperator on(Consumer<Conditional<?>> consumer) {
        Query<?, QueryParam> query = Query.of();
        consumer.accept(query);
        join.getTerms().addAll(query.getParam().getTerms());
        return this;
    }

    @SafeVarargs
    public final JoinOperator on(Supplier<Term>... conditions) {
        for (Supplier<Term> condition : conditions) {
            join.getTerms().add(condition.get());
        }
        return this;
    }

    @Override
    public Join get() {
        return join;
    }
}
