package com.cn.reactor.datagrap.api.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/21
 * @desc 数据库表属性
 */
@Data
public class TableEntity {
    /**
     * 表名
     */
    @Column(value = "table_name")
    private String tableName;
    /**
     * 索引类型
     */
    @Column(value = "engine")
    private String engine;
    /**
     * 表备注
     */
    @Column(value = "table_comment")
    private String tableComment;
    /**
     * 编码格式
     */
    @Column(value = "tableCollation")
    private String tablecollation;
    /**
     * 创建时间
     */
    @Column(value = "create_time")
    private LocalDateTime createTime;
}
