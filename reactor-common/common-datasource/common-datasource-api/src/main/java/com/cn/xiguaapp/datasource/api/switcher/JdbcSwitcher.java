package com.cn.xiguaapp.datasource.api.switcher;

/**
 * @author xiguaapp
 */
public interface JdbcSwitcher {
    Switcher datasource();

    Switcher schema();


}
