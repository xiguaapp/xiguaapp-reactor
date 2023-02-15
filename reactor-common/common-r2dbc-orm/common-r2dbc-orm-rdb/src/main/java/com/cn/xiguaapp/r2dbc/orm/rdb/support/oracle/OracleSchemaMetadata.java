package com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;

/**
 * @author xiguaapp
 */
public class OracleSchemaMetadata extends RDBSchemaMetadata {

    public OracleSchemaMetadata(String name) {
        super(name);
        addFeature(new OraclePaginator());

        addFeature(new OracleTableMetadataParser(this));

        addFeature(Dialect.ORACLE);
    }


    @Override
    public RDBTableMetadata newTable(String name) {
        RDBTableMetadata metadata = super.newTable(name);
        metadata.addFeature(OracleInsertSqlBuilder.of(metadata));
        return metadata;
    }
}
