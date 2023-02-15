package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:57
 */
public interface EntityTableMetadataResolver {
    RDBTableMetadata resolve(Class<?> entityClass);
}
