package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UpsertColumn extends InsertColumn {

    private boolean updateIgnore;

    public static UpsertColumn of(String column,boolean updateIgnore){
        UpsertColumn insertColumn=new UpsertColumn();
        insertColumn.setUpdateIgnore(updateIgnore);
        insertColumn.setColumn(column);

        return insertColumn;
    }
}
