package com.cn.xiguaapp.r2dbc.orm.rdb.executor.reactive;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc 响应式sql执行器
 */
public interface ReactiveSqlExecutor extends Feature {

    String ID_VALUE = "reactiveSqlExecutor";
    FeatureId<ReactiveSqlExecutor> ID = FeatureId.of(ID_VALUE);

    /**
     * 获取数据源id
     * @return
     */
    @Override
    default String getId() {
        return ID_VALUE;
    }

    /**
     * 获取数据源名称
     * @return
     */
    @Override
    default String getName() {
        return "响应式SQL执行器";
    }

    /**
     * 获取类型描述
     * @return
     */
    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.sqlExecutor;
    }

    /**
     * 更新
     * @param request
     * @return
     */
    Mono<Integer> update(Publisher<SqlRequest> request);

    /**
     * sql语句执行
     * @param request
     * @return
     */
    Mono<Void> execute(Publisher<SqlRequest> request);

    /**
     * 查询
     * @param request
     * @param wrapper
     * @param <E>
     * @return
     */
    <E> Flux<E> select(Publisher<SqlRequest> request, ResultWrapper<E, ?> wrapper);

    /**
     * sql语句执行
     * @param request
     * @return
     */
    default Mono<Void> execute(SqlRequest request) {
        return execute(Mono.just(request));
    }

    /**
     * 更新
     * @param request
     * @return
     */
    default Mono<Integer> update(SqlRequest request) {
        return update(Mono.just(request));
    }

    /**
     * 更新
     * @param sql
     * @param args
     * @return
     */
    default Mono<Integer> update(String sql, Object... args) {
        return update(SqlRequests.of(sql, args));
    }

    /**
     * 查询
     * @param sql
     * @param wrapper
     * @param <E>
     * @return
     */
    default <E> Flux<E> select(String sql, ResultWrapper<E, ?> wrapper) {
        return select(SqlRequests.of(sql), wrapper);
    }

    /**
     * 查询
     * @param sql
     * @param args
     * @return
     */
    default Flux<Map<String, Object>> select(String sql, Object... args) {
        return select(SqlRequests.of(sql, args), ResultWrappers.map());
    }

    /**
     * 查询
     * @param sqlRequest
     * @param wrapper
     * @param <E>
     * @return
     */
    default <E> Flux<E> select(SqlRequest sqlRequest, ResultWrapper<E, ?> wrapper) {
        return select(Mono.just(sqlRequest), wrapper);
    }

}
