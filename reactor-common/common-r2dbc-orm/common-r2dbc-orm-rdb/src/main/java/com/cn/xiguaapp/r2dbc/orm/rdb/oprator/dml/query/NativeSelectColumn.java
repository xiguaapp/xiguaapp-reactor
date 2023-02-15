package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@AllArgsConstructor
public class NativeSelectColumn extends SelectColumn implements NativeSql {
    private String sql;

    public static NativeSelectColumn of(String sql){
        return new NativeSelectColumn(sql);
    }

}
