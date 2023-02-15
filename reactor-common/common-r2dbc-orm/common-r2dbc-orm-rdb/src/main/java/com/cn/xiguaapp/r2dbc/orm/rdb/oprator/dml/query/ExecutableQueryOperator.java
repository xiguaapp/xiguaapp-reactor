package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query.QuerySqlBuilder;

/**
 * @author xiguaapp
 */
public class ExecutableQueryOperator extends BuildParameterQueryOperator {

    private final TableOrViewMetadata metadata;

    public ExecutableQueryOperator(TableOrViewMetadata metadata) {
        super(metadata.getName());
        this.metadata = metadata;
    }

    @Override
    public SqlRequest getSql() {
        return metadata.findFeatureNow(QuerySqlBuilder.ID).build(this.getParameter());
    }

    @Override
    public <E, R> QueryResultOperator<E, R> fetch(ResultWrapper<E, R> wrapper) {
        return new DefaultQueryResultOperator<>(this::getSql, metadata, wrapper);
    }
}
