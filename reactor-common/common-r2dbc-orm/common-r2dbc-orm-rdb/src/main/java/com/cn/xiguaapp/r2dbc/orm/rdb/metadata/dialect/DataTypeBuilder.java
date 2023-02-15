package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

/**
 * @author xiguaapp
 */
public interface DataTypeBuilder {
   String createColumnDataType(RDBColumnMetadata columnMetaData);
}
