package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NativeSqlUpdateColumn extends UpdateColumn implements NativeSql {

    private String sql;

    private Object[] parameters;

    public static NativeSqlUpdateColumn of(String column, String sql, Object... parameters) {
        NativeSqlUpdateColumn updateColumn = new NativeSqlUpdateColumn();
        updateColumn.setSql(sql);
        updateColumn.setColumn(column);
        updateColumn.setParameters(parameters);
        return updateColumn;
    }
}
