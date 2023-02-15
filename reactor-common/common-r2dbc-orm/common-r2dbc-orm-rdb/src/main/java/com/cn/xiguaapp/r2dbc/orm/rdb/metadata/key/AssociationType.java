package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssociationType {
    oneToOne(false),
    manyToOne(false),
    manyToMay(true),
    oneToMay(true),

    ;

    boolean toMany;
}
