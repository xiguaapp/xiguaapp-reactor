package com.cn.xiguaapp.common.gateway.utils;

import com.cn.xiguaapp.common.gateway.bean.ApiConfig;
import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.interceptor.DefaultRouteInterceptorContext;
import com.cn.xiguaapp.common.gateway.interceptor.RouteInterceptor;
import com.cn.xiguaapp.common.gateway.interceptor.RouteInterceptorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
@Slf4j
public class RouteInterceptorUtil {
    public static void runPreRoute(Object requestContext, ApiParam param, Consumer<RouteInterceptorContext> saveContext) {
        DefaultRouteInterceptorContext defaultRouteInterceptorContext = new DefaultRouteInterceptorContext();
        saveContext.accept(defaultRouteInterceptorContext);
        defaultRouteInterceptorContext.setBeginTimeMillis(System.currentTimeMillis());
        defaultRouteInterceptorContext.setRequestContext(requestContext);
        defaultRouteInterceptorContext.setApiParam(param);
        getRouteInterceptors().forEach(routeInterceptor -> {
            if (routeInterceptor.match(defaultRouteInterceptorContext)) {
                routeInterceptor.preRoute(defaultRouteInterceptorContext);
            }
        });
    }

    public static void runAfterRoute(RouteInterceptorContext routeInterceptorContext) {
        if (routeInterceptorContext == null) {
            return;
        }
        try {
            getRouteInterceptors().forEach(routeInterceptor -> {
                if (routeInterceptor.match(routeInterceptorContext)) {
                    routeInterceptor.afterRoute(routeInterceptorContext);
                }
            });
        } catch (Exception e) {
            log.error("执行路由拦截器异常, apiParam:{}", routeInterceptorContext.getApiParam().toJSONString());
        }
    }

    public static List<RouteInterceptor> getRouteInterceptors() {
        return ApiConfig.getInstance().getRouteInterceptors();
    }
//
    public static void addInterceptors(Collection<RouteInterceptor> interceptors) {
        List<RouteInterceptor> routeInterceptors = getRouteInterceptors();
        routeInterceptors.addAll(interceptors);
        routeInterceptors.sort(Comparator.comparing(RouteInterceptor::getOrder));
    }
}
