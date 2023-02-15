package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.ddl;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlterRequest {

    private RDBTableMetadata newTable;

    private RDBTableMetadata oldTable;

    private boolean allowDrop;

    private boolean allowAlter = true;

    private boolean allowIndexAlter = true;
}
