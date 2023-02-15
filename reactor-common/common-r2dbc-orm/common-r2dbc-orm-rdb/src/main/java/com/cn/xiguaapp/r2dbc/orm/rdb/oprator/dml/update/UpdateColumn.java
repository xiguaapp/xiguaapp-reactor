package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.FunctionColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateColumn extends FunctionColumn {

    private Object value;

}
