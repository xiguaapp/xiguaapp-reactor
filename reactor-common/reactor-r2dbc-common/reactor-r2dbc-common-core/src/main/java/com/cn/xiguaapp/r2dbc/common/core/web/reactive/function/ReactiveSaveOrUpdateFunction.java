package com.cn.xiguaapp.r2dbc.common.core.web.reactive.function;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.RecordCreationEntity;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.RecordModifierEntity;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
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
public interface ReactiveSaveOrUpdateFunction<E,K> {
    ReactiveRepository<E,K> getRepository();

    default E applyCreationEntity(E e){
        RecordCreationEntity creationEntity = ((RecordCreationEntity) e);
        creationEntity.setCreateTimeNow();
        return e;
    }

    default E applyModifierEntity(E entity) {
        RecordModifierEntity modifierEntity = ((RecordModifierEntity) entity);
        modifierEntity.setUpdateTimeNow();
        return entity;
    }
    default E applyEntity(E e){
        if (e instanceof RecordCreationEntity) {
            e = applyCreationEntity(e);
        }
        if (e instanceof RecordModifierEntity) {
            e = applyModifierEntity(e);
        }
        return e;
    }

    /**
     * 保存数据,如果传入了id,并且对应数据存在,则尝试覆盖,不存在则新增.
     * @return 数据流 {@link SaveResult}
     */
    default Function<Flux<E>, Mono<SaveResult>>saveOrUpdate(){
        return payload->payload.map(this::applyEntity).switchIfEmpty(payload).as(getRepository()::save);
    }

    /**
     * 批量新增数据
     * @return 数据流
     */
    default Function<Flux<E>,Mono<Integer>>saveBatch(){
        return payload->payload.map(this::applyEntity).switchIfEmpty(payload).collectList().as(getRepository()::insertBatch);
    }

    /**
     * 新增单个数据,并返回新增后的数据.
     * @return 数据流
     */
    default Function<Mono<E>,Mono<E>>insert(){
        return pay->pay.map(this::applyEntity)
                .switchIfEmpty(pay)
                .flatMap(en->getRepository()
                        .insert(Mono.just(en))
                        .thenReturn(en));
    }

    /**
     * 根据ID修改数据
     * @return boolean
     */
    default BiFunction<K,Mono<E>,Mono<Boolean>>update(){
        return (id,payload)->payload.map(this::applyEntity).switchIfEmpty(payload).flatMap(en->getRepository().updateById(id,Mono.just(en))).thenReturn(true);
    }

}
