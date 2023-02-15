package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.RecordCreationEntity;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.RecordModifierEntity;
import com.cn.xiguaapp.r2dbc.common.core.service.ReactiveCrudService;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.defaults.SaveResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:20
 */
public interface ReactiveServiceSaveOrUpdateFunction<T,ID> {
    ReactiveCrudService<T,ID> getService();

    default T applyCreationEntity(T e){
        RecordCreationEntity creationEntity = ((RecordCreationEntity) e);
        creationEntity.setCreateTimeNow();
        return e;
    }

    default T applyModifierEntity(T entity) {
        RecordModifierEntity modifierEntity = ((RecordModifierEntity) entity);
        modifierEntity.setUpdateTimeNow();
        return entity;
    }
    default T applyEntity(T e){
        if (e instanceof RecordCreationEntity) {
            e = applyCreationEntity(e);
        }
        if (e instanceof RecordModifierEntity) {
            e = applyModifierEntity(e);
        }
        return e;
    }

    /**
     * 保存数据，如果传入了id,并且对应数据存在,则尝试覆盖,不存在则新增
     */
    default Function<Flux<T>, Mono<SaveResult>>saveOrUpdate(){
        return payload->payload
                .map(this::applyEntity)
                .switchIfEmpty(payload)
                .as(getService()::save);
    }

    /**
     * 批量新增数据
     */
    default Function<Flux<T>,Mono<Integer>>saveBatch(){
        return payload->payload
                .map(this::applyEntity)
                .switchIfEmpty(payload)
                .collectList()
                .as(getService()::insertBatch);
    }

    /**
     * 新增单个数据,并返回新增后的数据.
     */
    default Function<Mono<T>,Mono<T>>insert(){
        return pay->pay.map(this::applyEntity)
                .switchIfEmpty(pay)
                .flatMap(en->getService()
                        .insert(Mono.just(en))
                        .thenReturn(en));
    }
    /**
     * 根据ID修改数据
     */
    default BiFunction<ID,Mono<T>,Mono<Boolean>>update(){
        return (id,payload)->payload
                .map(this::applyEntity)
                .switchIfEmpty(payload)
                .flatMap(en->getService()
                        .updateById(id,Mono.just(en)))
                .thenReturn(true);
    }

}
