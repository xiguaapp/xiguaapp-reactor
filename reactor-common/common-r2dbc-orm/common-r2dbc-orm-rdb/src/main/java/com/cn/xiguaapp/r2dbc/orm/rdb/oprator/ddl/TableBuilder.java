package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;

import java.util.function.Consumer;

/**
 * @author xiguaapp
 */
public interface TableBuilder {

    TableBuilder addColumn(RDBColumnMetadata column);

    TableBuilder custom(Consumer<RDBTableMetadata> consumer);

    ColumnBuilder addColumn();

    ColumnBuilder addColumn(String name);

    TableBuilder removeColumn(String name);

    TableBuilder dropColumn(String name);

    TableBuilder comment(String comment);

    TableBuilder alias(String name);

    TableBuilder allowAlter(boolean allow);

    TableBuilder autoLoad(boolean autoLoad);

    TableBuilder merge(boolean merge);

    IndexBuilder index();

    ForeignKeyDSLBuilder  foreignKey();

    TableDDLResultOperator commit();
}
