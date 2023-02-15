package com.cn.xiguaapp.datagrap.sqltemplate.service;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author xiguaapp
 * @date 2021-02-22
 * @desc sqltemplate服务
 */
public interface SqlTemplateService {
    /**
     * <p>
     *     从xml中获取sql模块并解析
     *     获取对应的sql语句
     *     参数1:
     *     参数2:
     * </p>
     * @return 获取对应的sql语句
     */
    BiFunction<String, Map<String,Object>,String>getSql();

    /**
     * <p>
     *     用户传入sql模板并解析
     *     获取对应的sql语句
     *     参数1:
     *     参数2:
     * </p>
     * @return sql语句
     */
    BiFunction<String,Map<String,Object>,String> getSqlFromStringTemplate();
}
