package com.cn.xiguaapp.r2dbc.orm.rdb.support.h2;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.BatchInsertSqlBuilder;

/**
 * @author xiguaapp
 */
public class H2SchemaMetadata extends RDBSchemaMetadata {

    public H2SchemaMetadata(String name) {
        super(name);
        addFeature(new H2CreateTableSqlBuilder());
        addFeature(new H2AlterTableSqlBuilder());
        addFeature(new H2Paginator());
        addFeature(new H2IndexMetadataParser(this));
        addFeature(new H2TableMetadataParser(this));
        addFeature(Dialect.H2);
    }

    @Override
    public RDBTableMetadata newTable(String name) {
        RDBTableMetadata metadata = super.newTable(name);
        metadata.addFeature(BatchInsertSqlBuilder.of(metadata));
        return metadata;
    }

}
