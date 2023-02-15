package com.cn.xiguapp.common.core.bean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 11:25
 */
public interface ToStringOperator<T> {
    default String toString(T target, String... ignoreProperty) {
        return toString(target, -1, ignoreProperty == null ? new java.util.HashSet<>() : new HashSet<>(Arrays.asList(ignoreProperty)));
    }

    String toString(T target, long features, Set<String> ignoreProperty);
}
