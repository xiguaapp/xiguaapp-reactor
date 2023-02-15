package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;


import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;

import java.util.Set;

/**
 * @author xiguaapp
 */
public interface ConstraintMetadata extends ObjectMetadata {

    @Override
    default RDBObjectType getObjectType() {
        return RDBObjectType.constraint;
    }

    Set<String> getColumns();

    boolean isPrimaryKey();

}
