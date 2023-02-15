package com.cn.xiguaapp.datasource.api.core;

import javax.sql.DataSource;

/**
 * 动态数据源
 *
 * @author xiguaapp
 * @since 3.0
 */
public interface JdbcDataSource extends DynamicDataSource<DataSource> {

    @Override
    DataSource getNative();
}
