package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class NativeSqlDefaultValue implements DefaultValue, NativeSql {
    private String sql;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public Object get() {
        return this;
    }
}
