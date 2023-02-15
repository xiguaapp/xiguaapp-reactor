package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 11:35
 */
@Getter
@Setter
public class PagerResult<E> {
    private static final long serialVersionUID = -6171751136953308027L;

    public static <E> PagerResult<E> empty() {
        return new PagerResult<>(0, new ArrayList<>());
    }

    public static <E> PagerResult<E> of(int total, List<E> list) {
        return new PagerResult<>(total, list);
    }

    public static <E> PagerResult<E> of(int total, List<E> list, QueryParam entity) {
        PagerResult<E> pagerResult = new PagerResult<>(total, list);
        pagerResult.setPageIndex(entity.getThinkPageIndex());
        pagerResult.setPageSize(entity.getPageSize());
        return pagerResult;
    }

    @Schema(description = "页码")
    private int pageIndex;

    @Schema(description = "每页数据量")
    private int pageSize;

    @Schema(description = "数据总量")
    private int total;

    @Schema(description = "数据列表")
    private List<E> data;

    public PagerResult() {
    }

    public PagerResult(int total, List<E> data) {
        this.total = total;
        this.data = data;
    }
}
