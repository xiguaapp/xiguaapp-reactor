package com.cn.xiguapp.common.core.engine;


import cn.hutool.script.JavaScriptEngine;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class DynamicScriptEngineFactory {
    private static final Map<String, DynamicScriptEngine> map = new HashMap<>();

    static {
        try {
            Class.forName("org.springframework.expression.ExpressionParser");
            map.put("spel", new SpElEngine());
        } catch (ClassNotFoundException e) {
        }
    }

    public static final DynamicScriptEngine getEngine(String type) {
        return map.get(type);
    }

}
