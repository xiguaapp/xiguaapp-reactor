package com.cn.xiguaapp.r2dbc.orm.rdb.mapping;


import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;

public interface DSLDelete<ME extends DSLDelete> extends Conditional<ME> {

     QueryParam toQueryParam();


}
