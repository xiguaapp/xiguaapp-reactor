package com.cn.xiguaapp.common.gateway.interceptor;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 路由拦截器
 */
public interface RouteInterceptor {
    /**
     * 在路由转发前执行，签名校验通过后会立即执行此方法
     *
     * @param context context
     */
    void preRoute(RouteInterceptorContext context);

    /**
     * 微服务返回结果后执行，可能返回成功，也可能返回失败结果，
     * 可通过 {@link RouteInterceptorContext#getResponseStatus()} 来判断是否返回正确结果。
     *
     * @param context context
     */
    void afterRoute(RouteInterceptorContext context);

    /**
     * 拦截器执行顺序，值小优先执行，建议从0开始，小于0留给系统使用
     *
     * @return 返回顺序
     */
    int getOrder();

    /**
     * 是否匹配，返回true执行拦截器，默认true
     * @param context context
     * @return 返回true执行拦截器
     */
    default boolean match(RouteInterceptorContext context) {
        return true;
    }

}
