package com.cn.xiguaapp.r2dbc.common.core.event;

import com.cn.xiguapp.common.core.core.proxy.FastBeanCopier;
import com.cn.xiguaapp.r2dbc.common.api.crud.entity.Entity;
import com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEntityEvent;
import com.cn.xiguaapp.r2dbc.orm.event.AsyncEvent;
import com.cn.xiguaapp.r2dbc.orm.event.GenericsPayloadApplicationEvent;
import com.cn.xiguaapp.r2dbc.orm.param.QueryParam;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.ContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventListener;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.*;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingEventTypes;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.ReactiveResultHolder;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

/**
 * @author xiguaapp
 * @desc 实体变更事件监听器
 * @since 1.0 17:34
 */
@SuppressWarnings("all")
public class EntityEventListener implements EventListener {
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public String getId() {
        return "entity-listener";
    }

    @Override
    public String getName() {
        return "实体变更事件监听器";
    }

    @Override
    public void onEvent(EventType type, EventContext context) {

        if (context.get(MappingContextKeys.error).isPresent()) {
            return;
        }
        EntityColumnMapping mapping = context.get(MappingContextKeys.columnMapping).orElse(null);
        if (mapping == null ||
                !Entity.class.isAssignableFrom(mapping.getEntityType()) ||
                mapping.getEntityType().getAnnotation(EnableEntityEvent.class) == null) {
            return;
        }

        if (type == MappingEventTypes.insert_before) {
            boolean single = context.get(MappingContextKeys.type).map("single"::equals).orElse(false);
            if (single) {
                handleSingleOperation(mapping.getEntityType(), context, EntityCreatedEvent::new);
            } else {
                handleBatchOperation(mapping.getEntityType(), context, EntityCreatedEvent::new);
            }
        }
        if (type == MappingEventTypes.save_before) {
            boolean single = context.get(MappingContextKeys.type).map("single"::equals).orElse(false);
            if (single) {
                handleSingleOperation(mapping.getEntityType(), context, EntitySavedEvent::new);
            } else {
                handleBatchOperation(mapping.getEntityType(), context, EntitySavedEvent::new);
            }
        }
        if (type == MappingEventTypes.update_before) {
            handleUpdateBefore(context);
        }

        if (type == MappingEventTypes.delete_before) {
            handleDeleteBefore(context);
        }
    }

    protected Mono<Void> sendUpdateEvent(List<?> olds, EventContext context) {
        List<Object> newValues = new ArrayList<>(olds.size());
        EntityColumnMapping mapping = context.get(MappingContextKeys.columnMapping).orElseThrow(UnsupportedOperationException::new);
        TableOrViewMetadata table = context.get(ContextKeys.table).orElseThrow(UnsupportedOperationException::new);
        RDBColumnMetadata idColumn = table.getColumns().stream().filter(RDBColumnMetadata::isPrimaryKey).findFirst().orElse(null);
        if (idColumn == null) {
            return Mono.empty();
        }
        for (Object old : olds) {
            Object newValue = context.get(MappingContextKeys.instance)
                    .filter(Entity.class::isInstance)
                    .map(Entity.class::cast)
                    .orElseGet(() -> {
                        return context.get(MappingContextKeys.updateColumnInstance)
                                .map(map -> {
                                    return FastBeanCopier.copy(map, FastBeanCopier.copy(old, mapping.getEntityType()));
                                })
                                .map(Entity.class::cast)
                                .orElse(null);
                    });
            if (newValue != null) {
                FastBeanCopier.copy(old, newValue, FastBeanCopier.include(idColumn.getAlias()));
            }
            newValues.add(newValue);
        }
        EntityModifyEvent event = new EntityModifyEvent(olds, newValues, mapping.getEntityType());
        eventPublisher.publishEvent(new GenericsPayloadApplicationEvent<>(this, event, mapping.getEntityType()));
        return event.getAsync();
    }

    protected Mono<Void> sendDeleteEvent(List<?> olds, EventContext context) {

        EntityColumnMapping mapping = context.get(MappingContextKeys.columnMapping).orElseThrow(UnsupportedOperationException::new);
        TableOrViewMetadata table = context.get(ContextKeys.table).orElseThrow(UnsupportedOperationException::new);

        EntityDeletedEvent deletedEvent = new EntityDeletedEvent(olds, mapping.getEntityType());
        eventPublisher.publishEvent(new GenericsPayloadApplicationEvent<>(this, deletedEvent, mapping.getEntityType()));
        return deletedEvent.getAsync();
    }

    protected void handleUpdateBefore(DSLUpdate<?, ?> update, EventContext context) {
        Object repo = context.get(MappingContextKeys.repository).orElse(null);
        if (repo instanceof ReactiveRepository) {
            context.get(MappingContextKeys.reactiveResultHolder)
                    .ifPresent(holder -> {
                        AtomicReference<List<?>> updated = new AtomicReference<>();
                        holder.after(v -> {
                            return Mono.defer(() -> {
                                List<?> _tmp = updated.getAndSet(null);

                                if (CollectionUtils.isNotEmpty(_tmp)) {
                                    return sendUpdateEvent(_tmp, context);
                                }
                                return Mono.empty();
                            });
                        });
                        holder.before(
                                ((ReactiveRepository<?, ?>) repo).createQuery()
                                        .setParam(update.toQueryParam())
                                        .fetch()
                                        .collectList()
                                        .doOnSuccess(updated::set)
                                        .then()
                        );
                    });
        }else if (repo instanceof SyncRepository) {
            QueryParam param = update.toQueryParam();
            SyncRepository<?, ?> syncRepository = ((SyncRepository<?, ?>) repo);
            List<?> list = syncRepository.createQuery()
                    .setParam(param)
                    .fetch();
            sendUpdateEvent(list,context).block();
        }
    }

    protected void handleUpdateBefore(EventContext context) {
        context.<DSLUpdate<?, ?>>get(ContextKeys.source())
                .ifPresent(dslUpdate -> {
                    handleUpdateBefore(dslUpdate, context);
                });

    }

    protected void handleDeleteBefore(EventContext context) {
        context.<DSLDelete>get(ContextKeys.source())
                .ifPresent(dslUpdate -> {
                    Object repo = context.get(MappingContextKeys.repository).orElse(null);
                    if (repo instanceof ReactiveRepository) {
                        context.get(MappingContextKeys.reactiveResultHolder)
                                .ifPresent(holder -> {
                                    AtomicReference<List<?>> deleted = new AtomicReference<>();
                                    holder.after(v -> {
                                        return Mono.defer(() -> {
                                            List<?> _tmp = deleted.getAndSet(null);
                                            if (CollectionUtils.isNotEmpty(_tmp)) {
                                                return sendDeleteEvent(_tmp, context);
                                            }
                                            return Mono.empty();
                                        });
                                    });
                                    holder.before(
                                            ((ReactiveRepository<?, ?>) repo).createQuery()
                                                    .setParam(dslUpdate.toQueryParam())
                                                    .fetch()
                                                    .collectList()
                                                    .doOnSuccess(deleted::set)
                                                    .then()
                                    );
                                });
                    } else if (repo instanceof SyncRepository) {
                        QueryParam param = dslUpdate.toQueryParam();
                        SyncRepository<?, ?> syncRepository = ((SyncRepository<?, ?>) repo);
                        List<?> list = syncRepository.createQuery()
                                .setParam(param)
                                .fetch();
                        sendDeleteEvent(list,context).block();
                    }
                });
    }

    protected void handleUpdateAfter(EventContext context) {

    }

    protected void handleBatchOperation(Class clazz, EventContext context, BiFunction<List<?>, Class, AsyncEvent> mapper) {

        context.get(MappingContextKeys.instance)
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .ifPresent(lst -> {
                    AsyncEvent event = mapper.apply(lst, clazz);
                    Object repo = context.get(MappingContextKeys.repository).orElse(null);
                    if (repo instanceof ReactiveRepository) {
                        Optional<ReactiveResultHolder> resultHolder = context.get(MappingContextKeys.reactiveResultHolder);
                        if (resultHolder.isPresent()) {
                            resultHolder
                                    .get()
                                    .after(v -> {
                                        eventPublisher.publishEvent(new GenericsPayloadApplicationEvent<>(this, event, clazz));
                                        return event.getAsync();
                                    });
                            return;
                        }
                    }
                    eventPublisher.publishEvent(new GenericsPayloadApplicationEvent<>(this, event, clazz));
                    //block非响应式的支持
                    event.getAsync().block();
                });
    }

    protected void handleSingleOperation(Class clazz, EventContext context, BiFunction<List<?>, Class, AsyncEvent> mapper) {
        context.get(MappingContextKeys.instance)
                .filter(Entity.class::isInstance)
                .map(Entity.class::cast)
                .ifPresent(entity -> {
                    AsyncEvent event = mapper.apply(Collections.singletonList(entity), clazz);
                    Object repo = context.get(MappingContextKeys.repository).orElse(null);
                    if (repo instanceof ReactiveRepository) {
                        Optional<ReactiveResultHolder> resultHolder = context.get(MappingContextKeys.reactiveResultHolder);
                        if (resultHolder.isPresent()) {
                            resultHolder
                                    .get()
                                    .after(v -> {
                                        eventPublisher.publishEvent(new GenericsPayloadApplicationEvent<>(this, event, clazz));
                                        return event.getAsync();
                                    });
                            return;
                        }
                    }
                    eventPublisher.publishEvent(new GenericsPayloadApplicationEvent<>(this, event, clazz));
                    //block非响应式的支持
                    event.getAsync().block();
                });
    }
}
