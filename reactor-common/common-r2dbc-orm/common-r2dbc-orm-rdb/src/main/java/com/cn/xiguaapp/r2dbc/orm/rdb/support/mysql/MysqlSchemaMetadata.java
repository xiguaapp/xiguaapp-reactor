package com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;

/**
 * @author xiguaapp
 */
public class MysqlSchemaMetadata extends RDBSchemaMetadata {

    public MysqlSchemaMetadata(String name) {
        super(name);

        addFeature(new MysqlCreateTableSqlBuilder());
        addFeature(new MysqlAlterTableSqlBuilder());
        addFeature(new MysqlPaginator());

        addFeature(new MysqlIndexMetadataParser(this));
        addFeature(new MysqlTableMetadataParser(this));
        addFeature(Dialect.MYSQL);
    }

    @Override
    public RDBTableMetadata newTable(String name) {
        RDBTableMetadata metadata= super.newTable(name);
        metadata.addFeature(new MysqlBatchUpsertOperator(metadata));
        return metadata;
    }

    @Override
    public void addTable(RDBTableMetadata metadata) {
        metadata.addFeature(new MysqlBatchUpsertOperator(metadata));
        super.addTable(metadata);
    }
}
