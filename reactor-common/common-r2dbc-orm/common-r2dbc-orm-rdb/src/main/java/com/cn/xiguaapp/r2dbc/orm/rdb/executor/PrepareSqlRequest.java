package com.cn.xiguaapp.r2dbc.orm.rdb.executor;

import com.cn.xiguaapp.r2dbc.orm.rdb.utils.SqlUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PrepareSqlRequest implements SqlRequest {

    private String sql;

    private Object[] parameters;

    @Override
    public boolean isEmpty() {
        return sql == null || sql.isEmpty();
    }

    public String toNativeSql() {

        return SqlUtils.toNativeSql(sql, parameters);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "empty sql";
        }
        return toNativeSql();
    }
}
