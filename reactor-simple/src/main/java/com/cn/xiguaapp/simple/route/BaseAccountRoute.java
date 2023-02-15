package com.cn.xiguaapp.simple.route;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.path.BuildPath;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.route.ReactiveCrudRoute;
import com.cn.xiguaapp.simple.entity.BaseAccount;
import com.cn.xiguaapp.simple.handler.BaseAccountHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @Date 2020/9/11
 * @desc
 */
@Configuration
public class BaseAccountRoute implements ReactiveCrudRoute<BaseAccount,Long> {
    @Autowired
    private BaseAccountHandler baseAccountHandler;
    @Override
    public BuildPath buildPath() {
        return BuildPath.builder().path("/baseAccount").build();
    }
    @Override
    public ReactiveCrudHandler<BaseAccount, Long> getReactiveCrudHandler() {
        return baseAccountHandler;
    }
}
