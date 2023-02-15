package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser;


import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityPropertyDescriptor;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;

/**
 * @author xiguaapp
 */
public interface DataTypeResolver {

    DataType resolve(EntityPropertyDescriptor descriptor);

}
