package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;


import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncUpdate;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update.UpdateOperator;

public class DefaultSyncUpdate<E> extends DefaultUpdate<E, SyncUpdate<E>> implements SyncUpdate<E> {

    public DefaultSyncUpdate(RDBTableMetadata table, UpdateOperator operator, EntityColumnMapping mapping) {
        super(table, operator, mapping);
    }

    @Override
    public int execute() {
        return doExecute().sync();
    }
}
