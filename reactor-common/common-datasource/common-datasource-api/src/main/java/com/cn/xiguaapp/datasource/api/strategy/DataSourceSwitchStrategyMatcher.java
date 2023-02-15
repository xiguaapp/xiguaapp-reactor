package com.cn.xiguaapp.datasource.api.strategy;


import com.cn.xiguapp.common.core.aop.MethodInterceptorContext;
import com.cn.xiguaapp.datasource.api.core.DynamicDataSource;
import com.cn.xiguaapp.datasource.api.exception.DataSourceNotFoundException;
import com.cn.xiguaapp.datasource.api.switcher.DataSourceSwitcher;

import java.lang.reflect.Method;

/**
 * 数据源切换策略,可通过此接口来自定义数据源切换的方式
 *
 * @author xiguaapp
 * @since 3.0.0-RC
 */
public interface DataSourceSwitchStrategyMatcher {

    /**
     * 匹配类和方法,返回是否需要进行数据源切换
     *
     * @param target 类
     * @param method 方法
     * @return 是否需要进行数据源切换
     */
    boolean match(Class target, Method method);

    /**
     * 获取数据源切换策略
     * @param context aop上下文
     * @return 切换策略
     */
    Strategy getStrategy(MethodInterceptorContext context);

    /**
     * 数据源切换策略
     */
    interface Strategy {
        /**
         * 是否使用默认数据源,与 {@link this#getDataSourceId}互斥,只在{@link this#getDataSourceId}不为空时生效
         *
         * @return 是否使用默认数据源
         */
        boolean isUseDefaultDataSource();

        /**
         * 当数据源不存在时,是否回退为默认数据源,如果为false,当数据源不存在时,将会抛出异常{@link DataSourceNotFoundException}
         *
         * @return 是否使用默认数据源
         * @see DataSourceNotFoundException
         */
        boolean isFallbackDefault();

        /**
         * @return 要切换数据源的id
         * @see DynamicDataSource#getId()
         * @see DataSourceSwitcher#use(String)
         */
        String getDataSourceId();

        /**
         * @since 3.0.8
         * @return 指定数据库
         */
        String getDatabase();
    }

}
