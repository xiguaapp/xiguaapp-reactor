package com.cn.xiguaapp.r2dbc.common.core.event;

import com.cn.xiguaapp.r2dbc.orm.event.DefaultAsyncEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiguaapp
 * @desc
 * @see com.cn.xiguaapp.r2dbc.common.core.annotation.EnableEntityEvent
 * @since 1.0 17:48
 */
@AllArgsConstructor
@Getter
public class EntityModifyEvent <E> extends DefaultAsyncEvent implements Serializable {

    private final List<E> before;

    private final List<E> after;

    private final Class<E> entityType;
}
