package com.cn.reactor.datagrap.api.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/20
 * @desc 数据库表字段名称 数据类型 描述 信息等
 */
@Data
public class ColumnEntity {
    /**
     * 列表
     */
    @Column(value = "column_name")
    private String columnName;

    /**
     * 数据类型
     */
    @Column(value = "data_type")
    private String dataType;

    /**
     * JAVA 数据类型
     */
    private String javaType;

    /**
     * 备注
     */
    @Column(value = "column_comment")
    private String comments;

    /**
     * 驼峰属性
     */
    @Column(value = "caseattr_name")
    private String caseattrName;

    /**
     * 普通属性
     */
    private String lowerattrName;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 其他信息
     */
    @Column(value = "extra")
    private String extra;

    /**
     * 字段类型
     */
    @Column(value = "column_type")
    private String columnType;

    /**
     * 是否可以为空
     */
    @Column(value = "is_nullable")
    private Boolean nullable;

    /**
     * 是否隐藏
     */
    @Column(value = "hidden")
    private Boolean hidden;
}
