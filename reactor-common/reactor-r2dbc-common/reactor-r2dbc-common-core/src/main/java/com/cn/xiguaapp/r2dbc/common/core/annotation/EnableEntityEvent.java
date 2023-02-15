package com.cn.xiguaapp.r2dbc.common.core.annotation;

import com.cn.xiguaapp.r2dbc.common.core.event.EntityCreatedEvent;
import com.cn.xiguaapp.r2dbc.common.core.event.EntityDeletedEvent;
import com.cn.xiguaapp.r2dbc.common.core.event.EntityModifyEvent;

import java.lang.annotation.*;

/**
 * @author xiguaapp
 * @desc 事件通知
 * @see EntityModifyEvent
 * @see EntityDeletedEvent
 * @see EntityCreatedEvent
 * @since 1.0 17:15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnableEntityEvent {
}
