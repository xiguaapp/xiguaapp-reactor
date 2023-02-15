package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;


import com.cn.xiguaapp.r2dbc.orm.meta.ObjectType;

/**
 * @author xiguaapp
 */
public class RDBViewMetadata extends AbstractTableOrViewMetadata {
    @Override
    public ObjectType getObjectType() {
        return RDBObjectType.view;
    }
}
