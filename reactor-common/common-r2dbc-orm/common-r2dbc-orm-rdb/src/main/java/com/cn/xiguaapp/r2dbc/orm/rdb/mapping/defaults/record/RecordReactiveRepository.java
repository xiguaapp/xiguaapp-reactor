package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record;

import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.DefaultReactiveRepository;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SimpleColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.DatabaseOperator;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public class RecordReactiveRepository<K> extends DefaultReactiveRepository<Record, K> {

    public RecordReactiveRepository(DatabaseOperator operator, String table) {
        this(operator,()->operator.getMetadata().getTable(table).orElseThrow(()->new UnsupportedOperationException("table [" + table + "] doesn't exist")));
    }

    public RecordReactiveRepository(DatabaseOperator operator, Supplier<RDBTableMetadata> table) {
        super(operator, table, Record.class, RecordResultWrapper.of(SimpleColumnMapping.of(DefaultRecord.class,table)));
    }

    @Override
    protected void initMapping(Class<Record> entityType) {

        this.mapping = SimpleColumnMapping.of(entityType,tableSupplier);
        defaultContextKeyValue.add(MappingContextKeys.columnMapping(mapping));

    }
}
