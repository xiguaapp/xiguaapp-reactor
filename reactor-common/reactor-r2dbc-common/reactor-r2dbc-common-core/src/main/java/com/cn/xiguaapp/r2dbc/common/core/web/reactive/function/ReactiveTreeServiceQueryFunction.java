package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryOperation;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryParamEntity;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.TreeSortSupportEntity;
import com.cn.xiguaapp.r2dbc.common.core.service.ReactiveTreeSortEntityService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:59
 */
public interface ReactiveTreeServiceQueryFunction<E extends TreeSortSupportEntity<K>,K> {

    ReactiveTreeSortEntityService<E,K>getService();

//    @QueryOperation(summary = "使用GET动态查询并返回树形结构")
    default Function<QueryParamEntity,Mono<List<E>>>findAllTreeGet(){
        return q->getService().queryResultToTree(q);
    }
    @QueryOperation(summary = "使用GET动态查询并返回子节点数据")
    default Function<QueryParamEntity,Flux<E>>findAllChildrenGet(){
        return q->getService().queryIncludeChildren(q);
    }
    @QueryOperation(summary = "使用GET动态查询并返回子节点树形结构数据")
    default Function<QueryParamEntity,Mono<List<E>>>findAllChildrenTreeGet(){
        return q->getService().queryIncludeChildrenTree(q);
    }

    @QueryOperation(summary = "使用Post动态查询并返回树形结构")
    default Function<Mono<QueryParamEntity>,Mono<List<E>>>findAllTreePost(){
        return q->getService().queryResultToTree(q);
    }
    @QueryOperation(summary = "使用post动态查询并返回子节点数据")
    default Function<Mono<QueryParamEntity>,Flux<E>>findAllChildrenPost(){
        return q->q.flatMapMany(pa->getService().queryIncludeChildren(pa));
    }
    @QueryOperation(summary = "使用post动态查询并返回子节点树形结构数据")
    default Function<Mono<QueryParamEntity>,Mono<List<E>>>findAllChildrenTreePost(){
        return p -> p.flatMap(pa->getService().queryIncludeChildrenTree(pa));
    }
}
