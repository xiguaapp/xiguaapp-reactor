package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.SortOrderSupplier;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.SortOrder;

@SuppressWarnings("all")
public interface DSLQuery<ME extends DSLQuery> extends Conditional<ME> {

    ME select(String... columns);

    ME selectExcludes(String... columns);

    <T> ME select(StaticMethodReferenceColumn<T>... column);

    <T> ME select(MethodReferenceColumn<T>... column);

    <T> ME selectExcludes(StaticMethodReferenceColumn<T>... column);

    <T> ME selectExcludes(MethodReferenceColumn<T>... column);

    ME paging(int pageIndex, int pageSize);

    ME orderBy(SortOrder... orders);

    ME orderBy(SortOrderSupplier... orders);

    ME setParam(QueryParam param);

}
