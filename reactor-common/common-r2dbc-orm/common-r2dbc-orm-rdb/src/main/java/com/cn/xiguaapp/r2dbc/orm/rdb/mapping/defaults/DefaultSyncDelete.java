package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;


import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.SyncDelete;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperator;

public class DefaultSyncDelete extends DefaultDelete<SyncDelete> implements SyncDelete {

    public DefaultSyncDelete(RDBTableMetadata table, DeleteOperator operator) {
        super(table,operator);
    }

    @Override
    public int execute() {
        return doExecute().sync();
    }
}
