package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

/**
 * @author xiguaapp
 * @desc 支持树形结构，排序的实体类，要使用树形结构，排序功能的实体类直接继承该类
 * @since 1.0 22:37
 */
public interface TreeSortSupportEntity <T> extends TreeSupportEntity<T>, SortSupportEntity {
}
