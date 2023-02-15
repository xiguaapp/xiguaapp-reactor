package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events;

import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ColumnWrapperContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import lombok.AllArgsConstructor;

import static com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys.tableMetadata;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys.columnWrapperContext;
import static com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys.result;

/**
 * @author xiguaapp
 */
@AllArgsConstructor
public class EventSupportWrapper<E, R> implements ResultWrapper<E, R> {

    private TableOrViewMetadata metadata;

    private ResultWrapper<E, R> wrapper;

    private ContextKeyValue<?>[] defaultKeyValues;

    public static <E, R> EventSupportWrapper<E, R> eventWrapper(TableOrViewMetadata metadata,
                                                                ResultWrapper<E, R> wrapper,
                                                                ContextKeyValue<?>... contextKeyValues) {
        return new EventSupportWrapper<>(metadata, wrapper, contextKeyValues);
    }

    @Override
    public E newRowInstance() {
        return wrapper.newRowInstance();
    }

    @Override
    public void wrapColumn(ColumnWrapperContext<E> context) {
        wrapper.wrapColumn(context);
        metadata.fireEvent(MappingEventTypes.select_wrapper_column, ctx -> ctx.set(columnWrapperContext(context), tableMetadata(metadata)).set(defaultKeyValues));
    }

    @Override
    public boolean completedWrapRow(E result) {
        boolean val = wrapper.completedWrapRow(result);
        metadata.fireEvent(MappingEventTypes.select_wrapper_done, ctx -> ctx.set(MappingContextKeys.instance(result), tableMetadata(metadata)).set(defaultKeyValues));
        return val;
    }

    @Override
    public R getResult() {
        R result = wrapper.getResult();
        metadata.fireEvent(MappingEventTypes.select_done, ctx -> ctx.set(result(result), tableMetadata(metadata)).set(defaultKeyValues));
        return result;
    }
}
