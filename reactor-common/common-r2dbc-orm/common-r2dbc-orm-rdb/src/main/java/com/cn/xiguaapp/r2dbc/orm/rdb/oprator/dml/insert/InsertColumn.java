package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.FunctionColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class InsertColumn extends FunctionColumn {


    public static InsertColumn of(String column){
        InsertColumn insertColumn=new InsertColumn();

        insertColumn.setColumn(column);

        return insertColumn;
    }
}
