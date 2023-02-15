package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.record;


import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.AbstractMapResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ColumnWrapperContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityColumnMapping;

import java.util.Optional;

public class RecordResultWrapper extends AbstractMapResultWrapper<Record> {

    public static RecordResultWrapper INSTANCE = new RecordResultWrapper();

    private EntityColumnMapping mapping;

    public static RecordResultWrapper of(EntityColumnMapping mapping) {
        RecordResultWrapper wrapper = new RecordResultWrapper();
        wrapper.mapping = mapping;
        return wrapper;
    }

    @Override
    public Record newRowInstance() {
        return new DefaultRecord();
    }

    @Override
    public void wrapColumn(ColumnWrapperContext<Record> context) {

        String property = Optional.ofNullable(mapping)
                .flatMap(mapping -> mapping.getPropertyByColumnName(context.getColumnLabel()))
                .orElse(context.getColumnLabel());

        Object value = Optional.ofNullable(mapping)
                .flatMap(mapping -> mapping.getColumnByProperty(property))
                .map(columnMetadata -> columnMetadata.decode(context.getResult()))
                .orElseGet(context::getResult);
        Record record = context.getRowInstance();

        super.doWrap(record, property, value);
    }

    @Override
    public Record getResult() {
        throw new UnsupportedOperationException();
    }
}
