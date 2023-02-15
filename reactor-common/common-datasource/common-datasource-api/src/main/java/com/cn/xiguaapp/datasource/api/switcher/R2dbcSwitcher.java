package com.cn.xiguaapp.datasource.api.switcher;

/**
 * @author xiguaapp
 */
public interface R2dbcSwitcher {
    ReactiveSwitcher datasource();

    ReactiveSwitcher schema();


}
