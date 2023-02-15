package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @desc 
 */
@Getter
public class BuildParameterDeleteOperator extends DeleteOperator {

    private DeleteOperatorParameter parameter = new DeleteOperatorParameter();


    @Override
    public DeleteOperator where(Consumer<Conditional<?>> dsl) {
        Query<?, QueryParam> query = Query.of();
        dsl.accept(query);

        parameter.getWhere()
                .addAll(query.getParam().getTerms());

        return this;
    }

    @Override
    public BuildParameterDeleteOperator where(Term term) {
        parameter.getWhere().add(term);
        return this;
    }

    @Override
    public SqlRequest getSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeleteResultOperator execute() {
        throw new UnsupportedOperationException();
    }
}
