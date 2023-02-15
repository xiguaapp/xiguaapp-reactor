package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiguaapp
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class DropIndexParameter {
    private RDBTableMetadata table;

    private RDBIndexMetadata index;
}
