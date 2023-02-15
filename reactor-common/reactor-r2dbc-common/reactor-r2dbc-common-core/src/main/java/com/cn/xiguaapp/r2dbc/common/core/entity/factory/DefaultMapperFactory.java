package com.cn.xiguaapp.r2dbc.common.core.entity.factory;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc 默认的实体映射
 * @since 1.0 15:49
 */
@FunctionalInterface
public interface DefaultMapperFactory extends Function<Class, MapperEntityFactory.Mapper> {
}
