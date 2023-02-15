/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午4:35 >
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

package com.cn.xiguaapp.common.gateway.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.bean.GatewayConstants;
import com.cn.xiguaapp.common.gateway.bean.ParamNames;
import com.cn.xiguaapp.common.gateway.param.FormHttpOutputMessage;
import com.cn.xiguaapp.common.gateway.route.ForwardInfo;
import com.cn.xiguaapp.common.gateway.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 */
@Slf4j
public class ServerWebExchangeUtil {

    private static final String THROWABLE_KEY = "sop.throwable";
    private static final String UNKNOWN_PATH = "/xiguaapp/unknown";

    private static final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    /**
     * 重定向
     *
     * @param exchange exchange
     * @param forwardInfo forwardInfo
     * @return 返回新的ServerWebExchange，配合chain.filter(newExchange);使用
     */
    public static ServerWebExchange getForwardExchange(ServerWebExchange exchange, ForwardInfo forwardInfo) {
        ServerHttpRequest.Builder builder = exchange.getRequest()
                .mutate();
        ServerHttpRequest newRequest = builder
                .header(ParamNames.HEADER_VERSION_NAME, forwardInfo.getVersion())
                .path(forwardInfo.getPath()).build();
        return exchange.mutate().request(newRequest).build();
    }


    /**
     * 构建一个接受请求体的request
     *
     * @param exchange exchange
     * @return 返回ServerRequest
     */
    public static ServerRequest createReadBodyRequest(ServerWebExchange exchange) {
        return ServerRequest.create(exchange, messageReaders);
    }

    public static String getRestfulPath(String path, String prefix) {
        int index = path.indexOf(prefix);
        // 取"/rest"的后面部分
        return path.substring(index + prefix.length());
    }

    /**
     * 重定向
     *
     * @param exchange    exchange
     * @param forwardPath 重定向path
     * @return 返回新的ServerWebExchange，配合chain.filter(newExchange);使用
     */
    public static ServerWebExchange getForwardExchange(ServerWebExchange exchange, String forwardPath) {
        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                .path(forwardPath).build();
        return exchange.mutate().request(newRequest).build();
    }

    public static Mono<Void> forwardUnknown(ServerWebExchange exchange, WebFilterChain chain) {
        // 非法访问
        ServerWebExchange newExchange = ServerWebExchangeUtil.getForwardExchange(exchange, UNKNOWN_PATH);
        return chain.filter(newExchange);
    }

    public static ApiParam getApiParamByQuery(ServerWebExchange exchange, String query) {
        ApiParam apiParam = new ApiParam();
        String ip = RequestUtil.getIP(exchange.getRequest());
        apiParam.setIp(ip);
        Map<String, ?> params = RequestUtil.parseQueryToMap(query);
        apiParam.putAll(params);
        setApiParam(exchange, apiParam);
        return apiParam;
    }

    public static ApiParam getApiParam(ServerWebExchange exchange, byte[] body) {
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        if (contentType == null) {
            contentType = MediaType.APPLICATION_FORM_URLENCODED;
        }
        ApiParam apiParam = new ApiParam();
        String ip = RequestUtil.getIP(exchange.getRequest());
        apiParam.setIp(ip);
        Map<String, ?> params = null;
        String contentTypeStr = contentType.toString().toLowerCase();
        // 如果是json方式提交
        if (StringUtils.containsAny(contentTypeStr, "json", "text")) {
            JSONObject jsonObject = JSON.parseObject(new String(body, GatewayConstants.CHARSET_UTF8));
            apiParam.putAll(jsonObject);
        } else if (StringUtils.containsIgnoreCase(contentTypeStr, "multipart")) {
            // 如果是文件上传请求
            HttpServletRequest fileUploadRequest = getFileUploadRequest(exchange, body);
            setFileUploadRequest(exchange, fileUploadRequest);
            RequestUtil.UploadInfo uploadInfo = RequestUtil.getUploadInfo(fileUploadRequest);
            params = uploadInfo.getUploadParams();
            apiParam.setUploadContext(uploadInfo.getUploadContext());
        } else {
            // APPLICATION_FORM_URLENCODED请求
            params = RequestUtil.parseQueryToMap(new String(body, GatewayConstants.CHARSET_UTF8));
        }
        if (params != null) {
            apiParam.putAll(params);
        }
        setApiParam(exchange, apiParam);
        return apiParam;
    }


    public static ApiParam getApiParam(ServerWebExchange exchange, Map<String, String> params) {
        ApiParam apiParam = new ApiParam();
        apiParam.putAll(params);
        setApiParam(exchange, apiParam);
        return apiParam;
    }

    public static void setThrowable(ServerWebExchange exchange, Throwable throwable) {
        exchange.getAttributes().put(THROWABLE_KEY, throwable);
    }

    public static Throwable getThrowable(ServerWebExchange exchange) {
        return (Throwable)exchange.getAttribute(THROWABLE_KEY);
    }

    /**
     * 获取请求参数
     *
     * @param exchange ServerWebExchange
     * @return 返回请求参数
     */
    public static ApiParam getApiParam(ServerWebExchange exchange) {
        return exchange.getAttribute(GatewayConstants.CACHE_API_PARAM);
    }

    /**
     * 设置请求参数
     *
     * @param exchange ServerWebExchange
     * @param apiParam 请求参数
     */
    public static void setApiParam(ServerWebExchange exchange, ApiParam apiParam) {
        exchange.getAttributes().put(GatewayConstants.CACHE_API_PARAM, apiParam);
    }

    /**
     * 添加header
     *
     * @param exchange        当前ServerWebExchange
     * @param headersConsumer headers
     * @return 返回一个新的ServerWebExchange
     */
    public static ServerWebExchange addHeaders(ServerWebExchange exchange, Consumer<HttpHeaders> headersConsumer) {
        // 创建一个新的request
        ServerHttpRequest serverHttpRequestNew = exchange.getRequest()
                .mutate()
                .headers(headersConsumer)
                .build();
        // 创建一个新的exchange对象
        return exchange
                .mutate()
                .request(serverHttpRequestNew)
                .build();
    }

    /**
     * 获取一个文件上传request
     *
     * @param exchange    当前ServerWebExchange
     * @param data 上传文件请求体内容
     * @return 返回文件上传request
     */
    public static HttpServletRequest getFileUploadRequest(ServerWebExchange exchange, byte[] data) {
        return new FileUploadHttpServletRequest(exchange.getRequest(), data);
    }

    public static HttpServletRequest getFileUploadRequest(ServerWebExchange exchange) {
        return exchange.getAttribute(GatewayConstants.CACHE_UPLOAD_REQUEST);
    }

    public static void setFileUploadRequest(ServerWebExchange exchange, HttpServletRequest request) {
        exchange.getAttributes().put(GatewayConstants.CACHE_UPLOAD_REQUEST, request);
    }

    /**
     * 修改请求参数。参考自：https://blog.csdn.net/fuck487/article/details/85166162
     *
     * @param exchange       ServerWebExchange
     * @param apiParam       原始请求参数
     * @param paramsConsumer 执行参数更改
     * @param headerConsumer header更改
     * @param <T>            参数类型
     * @return 返回新的ServerWebExchange
     */
    public static <T extends Map<String, Object>> ServerWebExchange format(
            ServerWebExchange exchange
            , T apiParam
            , Consumer<T> paramsConsumer
            , Consumer<HttpHeaders> headerConsumer
    ) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        // 新的request
        ServerHttpRequest newRequest;
        paramsConsumer.accept(apiParam);
        if (serverHttpRequest.getMethod() == HttpMethod.GET) {
            // 新的查询参数
            String queryString = RequestUtil.convertMapToQueryString(apiParam);
            // 创建一个新的request，并使用新的uri
            newRequest = new SopServerHttpRequestDecorator(serverHttpRequest, queryString);
        } else {
            MediaType mediaType = serverHttpRequest.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = MediaType.APPLICATION_FORM_URLENCODED;
            }
            String contentType = mediaType.toString().toLowerCase();
            // 修改后的请求体
            // 处理json请求（application/json）
            if (StringUtils.containsAny(contentType, "json", "text")) {
                String bodyStr = JSON.toJSONString(apiParam);
                byte[] bodyBytes = bodyStr.getBytes(StandardCharsets.UTF_8);
                newRequest = new SopServerHttpRequestDecorator(serverHttpRequest, bodyBytes);
            } else if (StringUtils.contains(contentType, "multipart")) {
                // 处理文件上传请求
                FormHttpOutputMessage outputMessage = new FormHttpOutputMessage();
                HttpServletRequest request = ServerWebExchangeUtil.getFileUploadRequest(exchange);
                try {
                    // 转成MultipartRequest
                    if (!(request instanceof MultipartHttpServletRequest)) {
                        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
                        request = commonsMultipartResolver.resolveMultipart(request);
                    }
                    // 重写新的值
                    MultiValueMap<String, Object> builder = RequestContentDataExtractor.extract(request);
                    for (Map.Entry<String, Object> entry : apiParam.entrySet()) {
                        Object value = entry.getValue();
                        if (value instanceof List) {
                            builder.put(entry.getKey(), (List) value);
                        } else {
                            builder.put(entry.getKey(), Collections.singletonList(String.valueOf(value)));
                        }
                    }
                    // 将字段以及上传文件重写写入到流中
                    formHttpMessageConverter.write(builder, mediaType, outputMessage);
                    // 获取新的上传文件流
                    byte[] bodyBytes = outputMessage.getInput();
                    newRequest = new SopServerHttpRequestDecorator(serverHttpRequest, bodyBytes);
                    // 必须要重新指定content-type，因为此时的boundary已经发生改变
                    MediaType contentTypeMultipart = outputMessage.getHeaders().getContentType();
                    newRequest.getHeaders().setContentType(contentTypeMultipart);
                } catch (IOException e) {
                    log.error("修改上传文件请求参数失败, apiParam:{}", apiParam, e);
                    throw new RuntimeException(e);
                }
            } else {
                // 否则一律按表单请求处理
                String bodyStr = RequestUtil.convertMapToQueryString(apiParam);
                byte[] bodyBytes = bodyStr.getBytes(StandardCharsets.UTF_8);
                newRequest = new SopServerHttpRequestDecorator(serverHttpRequest, bodyBytes);
            }
        }
        HttpHeaders headers = newRequest.getHeaders();
        // 自定义header
        headerConsumer.accept(headers);
        // 创建一个新的exchange
        return exchange.mutate().request(newRequest).build();
    }

}
