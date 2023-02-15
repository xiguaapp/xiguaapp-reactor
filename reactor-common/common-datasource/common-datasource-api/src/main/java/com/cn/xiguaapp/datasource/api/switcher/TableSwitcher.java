package com.cn.xiguaapp.datasource.api.switcher;

/**
 * 表切换器
 *
 * @author xiguaapp
 * @since 3.0.0-RC
 */
public interface TableSwitcher {
    void use(String source, String target);

    String getTable(String name);

    void reset();
}
