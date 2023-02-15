package com.cn.xiguaapp.r2dbc.orm.rdb.executor;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author xiguaapp
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class NullValue {
    @Deprecated
    private Class type;

    @NonNull
    private DataType dataType;

    public static NullValue of(DataType dataType){
        return of(dataType.getJavaType(),dataType);
    }

    @Override
    public String toString() {
        return "null" + (dataType==null?"": (type != null ? "(" + dataType.getId() + ")" : ""));
    }
}
