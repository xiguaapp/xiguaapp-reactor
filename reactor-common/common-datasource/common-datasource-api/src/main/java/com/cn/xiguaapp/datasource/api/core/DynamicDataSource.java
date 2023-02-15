package com.cn.xiguaapp.datasource.api.core;


import com.cn.xiguaapp.datasource.api.constant.DatabaseType;
import com.cn.xiguaapp.datasource.api.switcher.DataSourceSwitcher;

/**
 * 动态数据源
 *
 * @author xiguaapp
 * @since 3.0
 */
public interface DynamicDataSource<T> {

    /**
     * @return 数据源ID
     * @see DataSourceSwitcher#currentDataSourceId()
     */
    String getId();

    /**
     * @return 数据库类型
     * @see DatabaseType
     */
    DatabaseType getType();

    /**
     * @return 原始数据源
     */
    T getNative();

}
