package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@EqualsAndHashCode
public class FunctionColumn {
    private String column;

    private String function;

    private Map<String, Object> opts;

    @Override
    public String toString() {
        return function + "(" + column + ")";
    }
}
