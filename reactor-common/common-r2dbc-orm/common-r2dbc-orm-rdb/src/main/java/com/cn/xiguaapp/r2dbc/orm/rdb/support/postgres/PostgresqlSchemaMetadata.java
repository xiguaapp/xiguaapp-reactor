package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.CompositeExceptionTranslation;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.FeatureUtils;

/**
 * @author xiguaapp
 */
public class PostgresqlSchemaMetadata extends RDBSchemaMetadata {

    public PostgresqlSchemaMetadata(String name) {
        super(name);
        addFeature(new PostgresqlPaginator());
        addFeature(PostgresqlDropIndexSqlBuilder.INSTANCE);
        addFeature(new PostgresqlAlterTableSqlBuilder());

        addFeature(new PostgresqlTableMetadataParser(this));
        addFeature(new PostgresqlIndexMetadataParser(this));
        addFeature(Dialect.POSTGRES);

        addFeature(new CompositeExceptionTranslation()
                .add(FeatureUtils.r2dbcIsAlive(), () -> PostgresqlR2DBCExceptionTranslation.of(this))
        );
    }

    @Override
    public void addTable(RDBTableMetadata metadata) {
        metadata.addFeature(new PostgresqlBatchUpsertOperator(metadata));
        super.addTable(metadata);
    }

    @Override
    public RDBTableMetadata newTable(String name) {
        RDBTableMetadata metadata = super.newTable(name);
        metadata.addFeature(new PostgresqlBatchUpsertOperator(metadata));
        return metadata;
    }
}
