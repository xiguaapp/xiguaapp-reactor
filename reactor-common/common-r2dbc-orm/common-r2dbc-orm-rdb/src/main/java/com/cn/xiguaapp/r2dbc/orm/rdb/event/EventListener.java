package com.cn.xiguaapp.r2dbc.orm.rdb.event;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.DefaultFeatureType;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;

/**
 * @author xiguaapp
 */
public interface EventListener extends Feature {

    String ID_VALUE = "EventListener";

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "事件监听器";
    }

    @Override
    default FeatureType getType() {
        return DefaultFeatureType.eventListener;
    }

    FeatureId<EventListener> ID = FeatureId.of(ID_VALUE);

    void onEvent(EventType type, EventContext context);

}
