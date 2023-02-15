package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults;

import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeyValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveDelete;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete.DeleteOperator;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public class DefaultReactiveDelete extends DefaultDelete<ReactiveDelete> implements ReactiveDelete {
    public DefaultReactiveDelete(RDBTableMetadata tableMetadata,
                                 DeleteOperator operator,
                                 ContextKeyValue<?>... keyValues) {
        super(tableMetadata, operator,keyValues);
    }

    public BiFunction<ReactiveDelete, Mono<Integer>, Mono<Integer>> mapper =(reactiveDelete, integerMono) -> integerMono;

    @Override
    public Mono<Integer> execute() {
        return mapper.apply(this,doExecute().reactive());
    }

    @Override
    public ReactiveDelete onExecute(BiFunction<ReactiveDelete, Mono<Integer>, Mono<Integer>> mapper) {
        this.mapper = this.mapper.andThen(r->mapper.apply(this,r));
        return this;
    }
}
