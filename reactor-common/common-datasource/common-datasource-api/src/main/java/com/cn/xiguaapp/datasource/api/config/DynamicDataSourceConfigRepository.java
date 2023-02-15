package com.cn.xiguaapp.datasource.api.config;

import java.util.List;

/**
 * @author xiguaapp
 */
public interface DynamicDataSourceConfigRepository<C extends DynamicDataSourceConfig> {
    List<C> findAll();

    C findById(String dataSourceId);

    C add(C config);

    C remove(String dataSourceId);
}
