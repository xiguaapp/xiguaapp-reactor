package com.cn.xiguaapp.r2dbc.common.core.configuration;

import lombok.*;

/**
 * @author xiguaapp
 * @desc 实体基本信息
 * @since 1.0 11:49
 */
@Data
@EqualsAndHashCode(of = "entityType")
@AllArgsConstructor
public class EntityInfo {
    //实体
    private Class entityType;

    private Class realType;
    //id生成类型
    private Class idType;
    //是否支持响应式
    private boolean reactive;
}
