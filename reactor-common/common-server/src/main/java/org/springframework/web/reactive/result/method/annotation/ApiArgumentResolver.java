/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:13 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.springframework.web.reactive.result.method.annotation;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.cn.xiguaapp.common.starter.annotation.Open;
import com.cn.ykyoung.server.bean.OpenContext;
import com.cn.ykyoung.server.bean.OpenContextImpl;
import com.cn.ykyoung.server.bean.ParamNames;
import com.cn.ykyoung.server.bean.ServiceContext;
import com.cn.ykyoung.server.common.MyServletRequestWrapper;
import com.cn.ykyoung.server.common.SingleParameterWrapper;
import com.cn.ykyoung.server.common.param.ReactorHandlerMethodArgumentResolver;
import com.cn.ykyoung.server.common.param.SingleParameterContext;
import com.cn.ykyoung.server.common.param.SingleParameterContextValue;
import com.cn.ykyoung.server.common.utils.ParamValidator;
import com.cn.ykyoung.server.common.utils.ServiceParamValidator;
import com.cn.ykyoung.server.utils.FieldUtil;
import com.cn.ykyoung.server.utils.OpenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Parameter;
import java.security.Principal;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 * @desc 解析request参数中的业务参数 绑定到方法参数上
 * @since 1.0 17:13
 */
@Slf4j
public class ApiArgumentResolver implements ReactorHandlerMethodArgumentResolver {
    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>(256);

    private ParamValidator paramValidator = new ServiceParamValidator();

    private static Class<?> pushBuilder;

    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    static {
        try {
            pushBuilder = ClassUtils.forName("javax.servlet.http.PushBuilder",
                    ServerWebExchangeMethodArgumentResolver.class.getClassLoader());
        } catch (ClassNotFoundException ex) {
            // Servlet 4.0 PushBuilder not found - not supported for injection
            pushBuilder = null;
        }
    }

    public ApiArgumentResolver() {
        requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
    }

    @Override
    public Mono<Void> setRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        ArgumentResolverConfigurer argumentResolverConfigurer = requestMappingHandlerAdapter.getArgumentResolverConfigurer();
        Assert.isTrue(Objects.nonNull(argumentResolverConfigurer),"HandlerMethodArgumentResolver为空 执行失败");
        argumentResolverConfigurer.addCustomResolver(this);
        requestMappingHandlerAdapter.setArgumentResolverConfigurer(argumentResolverConfigurer);
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;

        return Mono.empty();
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Open open = methodParameter.getMethodAnnotation(Open.class);
        if (open == null) {
            return false;
        }
        Class<?> paramType = methodParameter.getParameterType();
        // 排除的
        boolean exclude = WebRequest.class.isAssignableFrom(paramType) || ServletRequest.class.isAssignableFrom(paramType) || MultipartRequest.class.isAssignableFrom(paramType) || HttpSession.class.isAssignableFrom(paramType) || pushBuilder.isAssignableFrom(paramType) || Principal.class.isAssignableFrom(paramType) || InputStream.class.isAssignableFrom(paramType) || Reader.class.isAssignableFrom(paramType) || HttpMethod.class == paramType || Locale.class == paramType || TimeZone.class == paramType || ZoneId.class == paramType || ServletResponse.class.isAssignableFrom(paramType) || OutputStream.class.isAssignableFrom(paramType) || Writer.class.isAssignableFrom(paramType);
        // 除此之外都匹配
        boolean support = !exclude;
        if (support) {
            this.wrapSingleParam(methodParameter, open);
        }
        return support;
    }

    /**
     * 包装单个值参数
     * @param methodParameter 参数信息
     * @param open open注解
     */
    private void wrapSingleParam(MethodParameter methodParameter, Open open) {
        Parameter parameter = methodParameter.getParameter();
        boolean isNumberStringEnumType = FieldUtil.isNumberStringEnumType(parameter.getType());
        if (isNumberStringEnumType) {
            log.debug("包装参数，方法：{}，参数名：{}", methodParameter.getMethod(), parameter.getName());
            SingleParameterContext.add(parameter, open);
        }
    }


    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        NativeWebRequest nativeWebRequest = new SopServletWebRequest(
                (HttpServletRequest) exchange.getRequest(),
                (HttpServletResponse) exchange.getResponse());
        Object paramObj = this.getParamObject(parameter, nativeWebRequest);
        if (paramObj != null) {
            // JSR-303验证

            if (paramObj instanceof SingleParameterWrapper) {
                SingleParameterWrapper parameterWrapper = (SingleParameterWrapper) paramObj;
                paramValidator.validateBizParam(parameterWrapper.getWrapperObject());
                return Mono.just(parameterWrapper.getParamValue());
            } else {
                paramValidator.validateBizParam(paramObj);
                return Mono.just(paramObj);
            }
        }
        HandlerMethodArgumentResolver resolver = getOtherArgumentResolver(parameter);
        if (resolver != null) {
            Object param = resolver.resolveArgument(
                    parameter
                    , bindingContext
                    , exchange
            );
            OpenContext openContext = ServiceContext.getCurrentContext().getOpenContext();
            if (openContext instanceof OpenContextImpl) {
                OpenContextImpl openContextImpl = (OpenContextImpl) openContext;
                openContextImpl.setBizObject(param);
            }
            return Mono.just(param);
        }
        return Mono.empty();
    }

    /**
     * 获取参数对象，将request中的参数绑定到实体对象中去
     *
     * @param methodParameter  方法参数
     * @param nativeWebRequest request
     * @return 返回参数绑定的对象，没有返回null
     */
    protected Object getParamObject(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        ServiceContext currentContext = ServiceContext.getCurrentContext();
        JSONObject requestParams = OpenUtil.getRequestParams(request);
        OpenContextImpl openContext = new OpenContextImpl(requestParams);
        currentContext.setOpenContext(openContext);
        String bizContent = requestParams.getString(ParamNames.BIZ_CONTENT_NAME);
        if (bizContent == null) {
            return null;
        }
        // 方法参数类型
        Class<?> parameterType = methodParameter.getParameterType();
        SingleParameterContextValue singleValue = SingleParameterContext.get(openContext.getMethod(), openContext.getVersion());
        // 如果是单值参数
        if (singleValue != null) {
            JSONObject jsonObj = JSON.parseObject(bizContent);
            Object paramValue = jsonObj.getObject(singleValue.getParameterName(), parameterType);
            Object wrapperObject = jsonObj.toJavaObject(singleValue.getWrapClass());
            SingleParameterWrapper singleParameterWrapper = new SingleParameterWrapper();
            singleParameterWrapper.setParamValue(paramValue);
            singleParameterWrapper.setWrapperObject(wrapperObject);
            return singleParameterWrapper;
        }
        Object param;
        // 如果是json字符串
        if (JSONValidator.from(bizContent).validate()) {
            param = JSON.parseObject(bizContent, parameterType);
        } else {
            // 否则认为是 aa=1&bb=33 形式
            Map<String, Object> query = OpenUtil.parseQueryToMap(bizContent);
            param = new JSONObject(query).toJavaObject(parameterType);
        }
        openContext.setBizObject(param);
        this.bindUploadFile(param, request);
        return param;
    }


    /**
     * 将上传文件对象绑定到属性中
     *
     * @param param              业务参数
     * @param httpServletRequest
     */
    protected void bindUploadFile(Object param, HttpServletRequest httpServletRequest) {
        if (httpServletRequest instanceof MyServletRequestWrapper) {
            httpServletRequest = (HttpServletRequest)((MyServletRequestWrapper) httpServletRequest).getRequest();
        }
        if (param == null) {
            return;
        }
        if (OpenUtil.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpServletRequest;
            Class<?> bizClass = param.getClass();
            ReflectionUtils.doWithFields(bizClass, field -> {
                ReflectionUtils.makeAccessible(field);
                String name = field.getName();
                MultipartFile multipartFile = request.getFile(name);
                ReflectionUtils.setField(field, param, multipartFile);
            }, field -> field.getType() == MultipartFile.class);
        }
    }

    /**
     * 获取其它的参数解析器
     *
     * @param parameter 业务参数
     * @return 返回合适参数解析器，没有返回null
     */
    protected HandlerMethodArgumentResolver getOtherArgumentResolver(MethodParameter parameter) {
        HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            ArgumentResolverConfigurer argumentResolverConfigurer = this.requestMappingHandlerAdapter.getArgumentResolverConfigurer();
            for (HandlerMethodArgumentResolver methodArgumentResolver : argumentResolverConfigurer.getCustomResolvers()) {
                if (methodArgumentResolver instanceof ReactorHandlerMethodArgumentResolver) {
                    continue;
                }
                if (methodArgumentResolver.supportsParameter(parameter)) {
                    result = methodArgumentResolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }

    public void setParamValidator(ParamValidator paramValidator) {
        this.paramValidator = paramValidator;
    }

    private static final class SopServletWebRequest extends ServletWebRequest {
        public SopServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
            super(new MyServletRequestWrapper(request), response);
        }
    }

}
