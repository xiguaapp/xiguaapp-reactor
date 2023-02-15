package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

import com.cn.xiguapp.common.core.exception.NotFoundException;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.PagerResult;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.QueryParamEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:53
 */
public interface ReactiveQueryFunction<E, K> {
    ReactiveRepository<E, K> getRepository();

    /**
     * 查询 不返回分页结果
     * <pre>GET /_query/no-paging?pageIndex=0&pageSize=20&where=name is 张三&orderBy=id desc</pre>
     *
     * @return 结果流
     * @see QueryParamEntity 查询条件
     */
//    @QueryOperation(summary = "使用get方式分页动态查询(不返回总数)", description = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false")
//    @ApiOperation(value = "此操作不返回分页总数，如果需要获取全部数据 请设置参数paging=false", notes = "query", response = QueryParamEntity.class)
    default Function<QueryParamEntity, Flux<E>> queryGet() {
        return this::query;
    }

    /**
     * POST方式查询.不返回分页结果
     *
     * <pre>
     *     POST /_query/no-paging
     *
     *     {
     *         "pageIndex":0,
     *         "pageSize":20,
     *         "where":"name like 张%", //放心使用,没有SQL注入
     *         "orderBy":"id desc",
     *         "terms":[ //高级条件
     *             {
     *                 "column":"name",
     *                 "termType":"like",
     *                 "value":"张%"
     *             }
     *         ]
     *     }
     * </pre>
     *
     * @return 结果流
     * @see QueryParamEntity 查询条件
     */
//    @Operation(summary = "使用POST方式分页动态查询(不返回总数)",
//            description = "此操作不返回分页总数,如果需要获取全部数据,请设置参数paging=false")
    default Function<Mono<QueryParamEntity>, Flux<E>> quertPost() {
        return query -> query.flatMapMany(this::query);
    }

    /**
     * GET方式分页查询
     *
     * <pre>
     *    GET /_query/no-paging?pageIndex=0&pageSize=20&where=name is 张三&orderBy=id desc
     * </pre>
     *
     * @return 分页查询结果
     * @see PagerResult 查询条件
     */
//    @QueryOperation(summary = "使用GET方式分页动态查询")
    default Function<QueryParamEntity, Mono<PagerResult<E>>> queryPagerGet() {
        return this::queryPager;
    }

//    @Operation(summary = "使用POST方式分页动态查询")
    @SuppressWarnings("all")
    @Bean
    default Function<Mono<QueryParamEntity>,Mono<PagerResult<E>>>queryPagerPost() {
        return query->query.flatMap(this::queryPager);
    }
    /**
     * 统计查询
     *
     * <pre>
     *     GET /_count
     * </pre>
     *
     * @see QueryParamEntity  查询条件
     * @return 统计结果
     */
//    @QueryOperation(summary = "使用GET方式查询总数")
    default Function<QueryParamEntity,Mono<Integer>>countGet(){
        return this::count;
    }
//    @Operation(summary = "使用POST方式查询总数")
    default Function<Mono<QueryParamEntity>,Mono<Integer>> count() {
        return query->query.flatMap(this::count);
    }

//    @Operation(summary = "根据ID查询")
    default Function<Mono<K>,Mono<E>>getById(){
        return id->getRepository()
                .findById(id)
                .switchIfEmpty(Mono.error(NotFoundException::new));
    }


    default Flux<E> query(QueryParamEntity queryParamEntity) {
        return getRepository().createQuery().setParam(queryParamEntity).fetch();
    }

    default Mono<PagerResult<E>> queryPager(QueryParamEntity query) {
        if (query.getTotal() != null) {
            return getRepository()
                    .createQuery()
                    .setParam(query.rePaging(query.getTotal()))
                    .fetch()
                    .collectList()
                    .map(list -> PagerResult.of(query.getTotal(), list, query));
        }

        return Mono.zip(
                getRepository().createQuery().setParam(query).count(),
                query(query.clone()).collectList(),
                (total, data) -> PagerResult.of(total, data, query)
        );
    }

    default Mono<Integer>count(QueryParamEntity queryParamEntity){
        return getRepository().createQuery().setParam(queryParamEntity).count();
    }
}
