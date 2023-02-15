package com.cn.xiguaapp.datasource.api.switcher;

/**
 * @author xiguaapp
 */
public class DefaultR2dbcSwicher implements R2dbcSwitcher {

    private DefaultReactiveSwitcher datasourceSwitcher=new DefaultReactiveSwitcher("r2dbc-datasource","datasource");
    private DefaultReactiveSwitcher schemaSwitcher=new DefaultReactiveSwitcher("r2dbc-schema","schema");


    @Override
    public ReactiveSwitcher datasource() {
        return datasourceSwitcher;
    }

    @Override
    public ReactiveSwitcher schema() {
        return schemaSwitcher;
    }
}
