package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class CreateIndexParameter {
    private RDBTableMetadata table;

    private RDBIndexMetadata index;
}
