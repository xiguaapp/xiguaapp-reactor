package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events;

import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MappingEventTypes implements EventType {
    select_before("查询前"),
    select_wrapper_column("查询列包装"),
    select_wrapper_done("查询包装"),
    select_done("查询完成"),

    update_before("修改之前"),
    update_after("修改之前"),

    insert_before("新增之前"),
    insert_after("新增之后"),

    delete_before("删除之前"),
    delete_after("删除之后"),

    save_before("修改之前"),
    save_after("修改之后"),
    ;

    private String name;

    @Override
    public String getId() {
        return name;
    }
}
