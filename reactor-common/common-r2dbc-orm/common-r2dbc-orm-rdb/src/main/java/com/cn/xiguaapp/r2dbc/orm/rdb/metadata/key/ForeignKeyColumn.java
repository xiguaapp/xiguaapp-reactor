package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;

/**
 * @author xiguaapp
 */
public interface ForeignKeyColumn {
    RDBColumnMetadata getTargetColumn();

    RDBColumnMetadata getSourceColumn();
}
