//package com.cn.xiguaapp.msg.service.core.route;
//
//import com.cn.xiguaapp.msg.service.core.handler.NotifyHistoryHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
///**
// * @author xiguaapp
// * @package_name xiguaapp-reactor
// * @Date 十一月
// * @desc
// */
//@Component
//public class NotifyRoute {
//    @Bean
//    public RouterFunction<ServerResponse> route(NotifyHistoryHandler handler){
//        return RouterFunctions
//                .route()
//                .POST("/notify/history",handler::insertHistory)
//                .build();
//    }
//}
