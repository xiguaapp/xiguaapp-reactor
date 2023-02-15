package com.cn.xiguapp.common.core.engine;

import com.cn.xiguapp.common.core.exception.ExecuteResult;

/**
 * @author xiguaapp
 */
public interface ScriptListener {
    void before(ScriptContext context);

    void after(ScriptContext context, ExecuteResult result);
}
