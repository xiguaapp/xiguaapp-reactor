package com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author xiguaapp
 * @Date 2020/9/9
 * @desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public abstract class SuperEntity {
    private @Id Long id;
    private @Column("is_del") int isDel;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private @Column("create_time") LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private @Column("update_time") LocalDateTime updateTime;
}
