package com.cn.xiguaapp.r2dbc.common.core.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc webflux注解支持 可进行自定义返回值封装等
 * @since 1.0 17:47
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class CommonWebFluxConfiguration {
}
