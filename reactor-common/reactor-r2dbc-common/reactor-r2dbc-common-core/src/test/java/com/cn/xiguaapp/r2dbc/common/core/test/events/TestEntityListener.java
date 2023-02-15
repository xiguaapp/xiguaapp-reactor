package com.cn.xiguaapp.r2dbc.common.core.test.events;

import com.cn.xiguaapp.r2dbc.common.core.event.EntityCreatedEvent;
import com.cn.xiguaapp.r2dbc.common.core.event.EntityDeletedEvent;
import com.cn.xiguaapp.r2dbc.common.core.event.EntityModifyEvent;
import com.cn.xiguaapp.r2dbc.common.core.event.EntitySavedEvent;
import com.cn.xiguaapp.r2dbc.common.core.test.entity.EventTestEntity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TestEntityListener {

    AtomicInteger created = new AtomicInteger();
    AtomicInteger deleted = new AtomicInteger();

    AtomicInteger modified = new AtomicInteger();

    AtomicInteger saved = new AtomicInteger();

    @EventListener
    public void handleCreated(EntityCreatedEvent<EventTestEntity> event) {
        event.async(Mono.fromRunnable(() -> {
            System.out.println(event);
            created.addAndGet(event.getEntity().size());
        }));
    }

    @EventListener
    public void handleCreated(EntityDeletedEvent<EventTestEntity> event) {
        event.async(Mono.fromRunnable(() -> {
            System.out.println(event);
            deleted.addAndGet(event.getEntity().size());
        }));
    }

    @EventListener
    public void handleModify(EntityModifyEvent<EventTestEntity> event) {
        event.async(Mono.fromRunnable(() -> {
            System.out.println(event);
            modified.addAndGet(event.getAfter().size());
        }));
    }

    @EventListener
    public void handleSave(EntitySavedEvent<EventTestEntity> event) {
        event.async(Mono.fromRunnable(() -> {
            System.out.println(event);
            saved.addAndGet(event.getEntity().size());
        }));
    }
}
