package com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;

/**
 * @author xiguaapp
 */
public class SqlServerSchemaMetadata extends RDBSchemaMetadata {

    public SqlServerSchemaMetadata(String name) {
        super(name);
        addFeature(new SqlServerCreateTableSqlBuilder());
        addFeature(new SqlServerAlterTableSqlBuilder());
        addFeature(new SqlServer2012Paginator());
        addFeature(new SqlServer2012TableMetadataParser(this));
        addFeature(Dialect.MSSQL);
    }
}
