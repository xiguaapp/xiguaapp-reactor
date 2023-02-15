package com.cn.xiguaapp.reactive.cache.support;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:14
 */
public class NullValue implements Serializable {
    @Serial
    private static final long serialVersionUID = -1;

    public static final NullValue INSTANCE = new NullValue();
}
