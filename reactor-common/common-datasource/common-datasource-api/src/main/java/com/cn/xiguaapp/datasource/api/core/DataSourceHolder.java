package com.cn.xiguaapp.datasource.api.core;

import com.cn.xiguaapp.datasource.api.constant.DatabaseType;
import com.cn.xiguaapp.datasource.api.exception.DataSourceNotFoundException;
import com.cn.xiguaapp.datasource.api.switcher.DefaultJdbcSwitcher;
import com.cn.xiguaapp.datasource.api.switcher.DefaultR2dbcSwicher;
import com.cn.xiguaapp.datasource.api.switcher.JdbcSwitcher;
import com.cn.xiguaapp.datasource.api.switcher.R2dbcSwitcher;
import reactor.core.publisher.Mono;

/**
 * 用于操作动态数据源,如获取当前使用的数据源,使用switcher切换数据源等
 *
 * @author xiguaapp
 * @since 3.0
 */
public final class DataSourceHolder {


    /**
     * 动态数据源服务
     */
    public static volatile DynamicDataSourceService dynamicDataSourceService;

    public static volatile JdbcSwitcher jdbcSwitcher = new DefaultJdbcSwitcher();
    public static volatile R2dbcSwitcher r2dbcSwitcher = new DefaultR2dbcSwicher();

    public static boolean isDynamicDataSourceReady() {
        return dynamicDataSourceService != null;
    }

    public static void checkDynamicDataSourceReady() {
        if (dynamicDataSourceService == null) {
            throw new UnsupportedOperationException("dataSourceService not ready");
        }
    }

    /**
     * @return 动态数据源切换器
     */
    public static JdbcSwitcher switcher() {
        return jdbcSwitcher;
    }

    public static R2dbcSwitcher r2dbcSwitcher() {
        return r2dbcSwitcher;
    }

    /**
     * @return 默认数据源
     */
    public static JdbcDataSource defaultDataSource() {
        checkDynamicDataSourceReady();
        return (JdbcDataSource) dynamicDataSourceService.getDefaultDataSource();
    }

    /**
     * 根据指定的数据源id获取动态数据源
     *
     * @param dataSourceId 数据源id
     * @return 动态数据源
     * @throws DataSourceNotFoundException 如果数据源不存在将抛出此异常
     */
    public static JdbcDataSource dataSource(String dataSourceId) {
        checkDynamicDataSourceReady();
        return dynamicDataSourceService.getDataSource(dataSourceId);
    }


    /**
     * @return 当前使用的数据源
     */
    public static JdbcDataSource currentDataSource() {
        return jdbcSwitcher.datasource()
                .current()
                .map(dynamicDataSourceService::getDataSource)
                .orElseGet(DataSourceHolder::defaultDataSource);
    }

    public static Mono<R2dbcDataSource> currentR2dbc() {
        return r2dbcSwitcher.datasource()
                .current()
                .flatMap(dynamicDataSourceService::getR2dbcDataSource)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.just(dynamicDataSourceService.getDefaultDataSource())
                                .map(R2dbcDataSource.class::cast)));
    }

    /**
     * @return 当前使用的数据源是否为默认数据源
     */
    public static boolean currentIsDefault() {
        return !jdbcSwitcher.datasource().current().isPresent();
    }

    /**
     * 判断指定id的数据源是否存在
     *
     * @param id 数据源id {@link DynamicDataSource#getId()}
     * @return 数据源是否存在
     */
    public static boolean existing(String id) {
        try {
            checkDynamicDataSourceReady();
            return dynamicDataSourceService.getDataSource(id) != null;
        } catch (DataSourceNotFoundException e) {
            return false;
        }
    }

    /**
     * @return 当前使用的数据源是否存在
     */
    public static boolean currentExisting() {
        if (currentIsDefault()) {
            return true;
        }
        try {
            return currentDataSource() != null;
        } catch (DataSourceNotFoundException e) {
            return false;
        }
    }

    /**
     * @return 当前数据库类型
     */
    public static DatabaseType currentDatabaseType() {
        return currentDataSource().getType();
    }

    /**
     * @return 默认的数据库类型
     */
    public static DatabaseType defaultDatabaseType() {
        return defaultDataSource().getType();
    }
}
