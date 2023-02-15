package com.cn.xiguaapp.r2dbc.common.core.event;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.Entity;
import com.cn.xiguaapp.r2dbc.common.core.validator.CreateGroup;
import com.cn.xiguaapp.r2dbc.common.core.validator.UpdateGroup;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventListener;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventType;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingContextKeys;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.events.MappingEventTypes;

import java.util.List;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:51
 */
public class ValidateEventListener implements EventListener {
    @Override
    public String getId() {
        return "validate-listener";
    }

    @Override
    public String getName() {
        return "验证器监听器";
    }

    @Override
    @SuppressWarnings("all")
    public void onEvent(EventType type, EventContext context) {
        if (type == MappingEventTypes.insert_before || type == MappingEventTypes.save_before) {

            boolean single = context.get(MappingContextKeys.type).map("single"::equals).orElse(false);
            if (single) {
                context.get(MappingContextKeys.instance)
                        .filter(Entity.class::isInstance)
                        .map(Entity.class::cast)
                        .ifPresent(entity -> entity.tryValidate(CreateGroup.class));
            } else {
                context.get(MappingContextKeys.instance)
                        .filter(List.class::isInstance)
                        .map(List.class::cast)
                        .ifPresent(lst -> lst.stream()
                                .filter(Entity.class::isInstance)
                                .map(Entity.class::cast)
                                .forEach(e -> ((Entity) e).tryValidate(CreateGroup.class))
                        );
            }

        } else if (type == MappingEventTypes.update_before) {
            context.get(MappingContextKeys.instance)
                    .filter(Entity.class::isInstance)
                    .map(Entity.class::cast)
                    .ifPresent(entity -> entity.tryValidate(UpdateGroup.class));
        }

    }
}
