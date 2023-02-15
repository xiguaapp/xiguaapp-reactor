package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author xiguaapp
 * @desc 支持树形结构，排序的实体类，要使用树形结构，排序功能的实体类直接继承该类
 * @since 1.0 11:33
 */
@Getter
@Setter
public abstract class GenericTreeSortSupportEntity<ID>extends GenericEntity<ID> implements TreeSortSupportEntity<ID> {
    /**
     * 父级类别
     */
    @Column(name = "parent_id", length = 32)
    @Comment("父级ID")
    @Schema(description = "父节点ID")
    private ID parentId;

    /**
     * 树结构编码,用于快速查找, 每一层由4位字符组成,用-分割
     * 如第一层:0001 第二层:0001-0001 第三层:0001-0001-0001
     */
    @Column(name = "path", length = 128)
    @Comment("树路径")
    @Schema(description = "树结构路径")
    private String path;

    /**
     * 排序索引
     */
    @Column(name = "sort_index", precision = 32)
    @Comment("排序序号")
    @Schema(description = "排序序号")
    private Long sortIndex;

    @Column(name = "_level", precision = 32)
    @Comment("树层级")
    @Schema(description = "树层级")
    private Integer level;
}
