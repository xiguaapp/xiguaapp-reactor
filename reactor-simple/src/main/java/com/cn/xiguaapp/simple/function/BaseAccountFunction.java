package com.cn.xiguaapp.simple.function;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.ReactiveRepository;
import com.cn.xiguaapp.simple.entity.BaseAccount;
import com.cn.xiguaapp.simple.service.BaseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 18:52
 */
@Configuration
public class BaseAccountFunction implements ReactiveCrudFunction<BaseAccount,Long> {
    @Autowired
    private BaseAccountService baseAccountService;

    @Override
    public ReactiveRepository<BaseAccount, Long> getRepository() {
        return baseAccountService.getRepository();
    }
}
