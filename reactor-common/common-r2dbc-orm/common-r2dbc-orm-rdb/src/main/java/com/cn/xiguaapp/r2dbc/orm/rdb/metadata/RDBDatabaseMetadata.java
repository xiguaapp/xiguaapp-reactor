package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.meta.AbstractDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author xiguaapp
 * @desc db实体基本信息
 */
public class RDBDatabaseMetadata extends AbstractDatabaseMetadata<RDBSchemaMetadata> {
    protected Dialect dialect;

    public RDBDatabaseMetadata(Dialect dialect) {
        this.dialect = dialect;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public Optional<TableOrViewMetadata> getTableOrView(String name) {
        return this.getObject(name, RDBSchemaMetadata::getTableOrView);
    }

    public Optional<RDBTableMetadata> getTable(String name) {
        return this.getObject(name, RDBSchemaMetadata::getTable);
    }

    public Mono<TableOrViewMetadata> getTableOrViewReactive(String name) {
        return this.getObjectReactive(name, RDBSchemaMetadata::getTableOrViewReactive);
    }

    public Mono<RDBTableMetadata> getTableReactive(String name) {
        return this.getObjectReactive(name, RDBSchemaMetadata::getTableReactive);
    }

    @Override
    public Optional<RDBSchemaMetadata> getSchema(String name) {
        return super.getSchema(getDialect().clearQuote(name));
    }

    @Override
    public void addSchema(RDBSchemaMetadata schema) {
        schema.setDatabase(this);
        super.addSchema(schema);
    }
}
