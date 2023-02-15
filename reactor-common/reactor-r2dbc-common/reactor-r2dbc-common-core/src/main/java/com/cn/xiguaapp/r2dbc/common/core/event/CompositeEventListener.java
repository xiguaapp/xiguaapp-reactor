package com.cn.xiguaapp.r2dbc.common.core.event;

import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventListener;
import com.cn.xiguaapp.r2dbc.orm.rdb.event.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xiguaapp
 * @desc 事件监听
 * @since 1.0 17:32
 */
@Getter
@Setter
public class CompositeEventListener implements EventListener {
    private List<EventListener> eventListeners = new CopyOnWriteArrayList<>();

    @Override
    public void onEvent(EventType type, EventContext context) {
        for (EventListener eventListener : eventListeners) {
            eventListener.onEvent(type, context);
        }
    }

    public void addListener(EventListener eventListener) {
        eventListeners.add(eventListener);
    }
}
