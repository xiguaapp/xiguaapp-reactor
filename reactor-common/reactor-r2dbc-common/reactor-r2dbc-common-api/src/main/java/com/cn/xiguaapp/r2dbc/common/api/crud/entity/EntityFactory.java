package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguapp.common.core.exception.NotFoundException;

/**
 * @author xiguaapp
 * @desc 实体工厂接口,系统各个地方使用此接口来创建实体,在实际编码中也应该使用此接口来创建实体,而不是使用new方式来创建
 * @since 1.0 22:23
 */
public interface EntityFactory {
    /**
     * 根据类型创建实例
     * <p>
     * e.g.
     * <pre>
     *  entityFactory.newInstance(UserEntity.class);
     * </pre>
     *
     * @param entityClass 要创建的class
     * @param <T>         类型
     * @return 创建结果
     */
    <T> T newInstance(Class<T> entityClass) throws NotFoundException;


    /**
     * 根据类型创建实例,如果类型无法创建,则使用默认类型进行创建
     * <p>
     * e.g.
     * <pre>
     *  entityFactory.newInstance(UserEntity.class,SimpleUserEntity.class);
     * </pre>
     *
     * @param entityClass  要创建的class
     * @param defaultClass 默认class,当{@code entityClass}无法创建时使用此类型进行创建
     * @param <T>          类型
     * @return 实例
     */
    <T> T newInstance(Class<T> entityClass, Class<? extends T> defaultClass) throws NotFoundException;

    /**
     * 创建实体并设置默认的属性
     *
     * @param entityClass       实体类型
     * @param defaultProperties 默认属性
     * @param <S>               默认属性的类型
     * @param <T>               实体类型
     * @return 创建结果
     * @see EntityFactory#copyProperties(Object, Object)
     */
    default <S, T> T newInstance(Class<T> entityClass, S defaultProperties) throws NotFoundException {
        return copyProperties(defaultProperties, newInstance(entityClass));
    }

    /**
     * 创建实体并设置默认的属性
     *
     * @param entityClass       实体类型
     * @param defaultClass      默认class
     * @param defaultProperties 默认属性
     * @param <S>               默认属性的类型
     * @param <T>               实体类型
     * @return 创建结果
     * @see EntityFactory#copyProperties(Object, Object)
     */
    default <S, T> T newInstance(Class<T> entityClass, Class<? extends T> defaultClass, S defaultProperties) throws NotFoundException {
        return copyProperties(defaultProperties, newInstance(entityClass, defaultClass));
    }


    /**
     * 根据类型获取实体的真实的实体类型,
     * 可通过此方法获取获取已拓展的实体类型，如:<br>
     * <code>
     * factory.getInstanceType(MyBeanInterface.class);
     * </code>
     *
     * @param entityClass 类型
     * @param <T>         泛型
     * @return 实体类型
     */
    default  <T> Class<T> getInstanceType(Class<T> entityClass){
        return getInstanceType(entityClass,false);
    }

    <T> Class<T> getInstanceType(Class<T> entityClass,boolean autoRegister);

    /**
     * 拷贝对象的属性
     *
     * @param source 要拷贝到的对象
     * @param target 被拷贝的对象
     * @param <S>    要拷贝对象的类型
     * @param <T>    被拷贝对象的类型
     * @return 被拷贝的对象
     */
    <S, T> T copyProperties(S source, T target);
}
