package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.dsl.Query;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author xiguaapp
 */
@Getter
public class BuildParameterUpdateOperator extends UpdateOperator {

    private UpdateOperatorParameter parameter = new UpdateOperatorParameter();

    @Override
    public UpdateOperator set(String column, Object value) {
        UpdateColumn updateColumn = new UpdateColumn();
        updateColumn.setColumn(column);
        updateColumn.setValue(value);
        parameter.getColumns().add(updateColumn);
        return this;
    }

    @Override
    public UpdateOperator set(Object entity) {
        // TODO: 2019-09-04
        return this;
    }

    @Override
    public UpdateOperator set(UpdateColumn column) {
        parameter.getColumns().add(column);
        return this;
    }

    @Override
    public UpdateOperator where(Consumer<Conditional<?>> dsl) {
        Query<?, QueryParam> query = Query.of();
        dsl.accept(query);

        parameter.getWhere()
                .addAll(query.getParam().getTerms());

        return this;
    }

    @Override
    public UpdateOperator where(Term term) {
        parameter.getWhere().add(term);
        return this;
    }

    @Override
    public SqlRequest getSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UpdateResultOperator execute() {
        throw new UnsupportedOperationException();
    }
}
