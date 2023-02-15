package com.cn.xiguaapp.simple.handler;

import com.cn.xiguaapp.r2dbc.common.core.web.reactive.function.ReactiveCrudFunction;
import com.cn.xiguaapp.r2dbc.common.core.web.reactive.handler.ReactiveCrudHandler;
import com.cn.xiguaapp.simple.entity.BaseAccount;
import com.cn.xiguaapp.simple.function.BaseAccountFunction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author xiguaapp
 * @Date 2020/9/11
 * @desc
 */
@Component
@AllArgsConstructor
public class BaseAccountHandler implements ReactiveCrudHandler<BaseAccount,Long> {
    private final BaseAccountFunction baseAccountFunction;

    @Override
    public ReactiveCrudFunction<BaseAccount, Long> getReactiveCrudFunction() {
        return baseAccountFunction;
    }


//    public Mono<ServerResponse> queryPager(ServerRequest request){
//       return ServerResponse.ok().body(baseAccountFunction.queryGet()
//               .apply(QueryParamEntity.of())
//               ,BaseAccount.class);
//   }
}
