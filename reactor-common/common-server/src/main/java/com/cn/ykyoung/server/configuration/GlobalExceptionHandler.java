/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午6:16 >
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

package com.cn.ykyoung.server.configuration;

import com.cn.xiguapp.common.core.exception.ServiceResultBuilder;
import com.cn.ykyoung.server.bean.ServiceConfig;
import com.cn.ykyoung.server.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author xiguaapp
 * @desc 全局异常
 * @since 1.0 18:16
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 与网关约定好的状态码，表示业务出错
     */
    private static final int BIZ_ERROR_CODE = 4000;

    /**
     * 与网关约定好的系统错误状态码
     */
    private static final int SYSTEM_ERROR_CODE = 5050;

    /**
     * header中的错误code
     */
    private static final String X_SERVICE_ERROR_HEADER_NAME = "x-service-error-code";

    /**
     * header中的错误信息
     */
    private static final String X_SERVICE_ERROR_MESSAGE = "x-service-error-message";

    /**
     * 捕获手动抛出的异常
     *
     * @param request   request
     * @param response  response
     * @param exception 异常信息
     * @return 返回提示信息
     */
    @ExceptionHandler(ServiceException.class)
    public Object serviceExceptionHandler(ServerRequest request, ServerResponse response, Exception exception) {
        response.headers().add(X_SERVICE_ERROR_HEADER_NAME, String.valueOf(BIZ_ERROR_CODE));
        return this.processError(request, response, exception);
    }

    /**
     * 捕获未知异常
     *
     * @param request   request
     * @param response  response
     * @param exception 异常信息
     * @return 返回提示信息
     */
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(ServerRequest request, ServerResponse response, Exception exception) {
        log.error("系统错误", exception);
        StringBuilder msg = new StringBuilder();
        msg.append(exception.getMessage());
        StackTraceElement[] stackTrace = exception.getStackTrace();
        // 取5行错误内容
        int lineCount = 5;
        for (int i = 0; i < stackTrace.length && i < lineCount; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            msg.append("\n at ").append(stackTraceElement.toString());
        }
        // 需要设置两个值，这样网关会收到错误信息
        // 并且会统计到监控当中
        response.headers().add(X_SERVICE_ERROR_HEADER_NAME, String.valueOf(SYSTEM_ERROR_CODE));
        response.headers().add(X_SERVICE_ERROR_MESSAGE, UriUtils.encode(msg.toString(), StandardCharsets.UTF_8));
        return this.processError(request, response, new ServiceException("系统繁忙"));
    }

    /**
     * 处理异常
     *
     * @param request   request
     * @param response  response
     * @param throwable 异常信息
     * @return 返回最终结果
     */
    protected Object processError(ServerRequest request, ServerResponse response, Throwable throwable) {
        ServiceResultBuilder serviceResultBuilder = ServiceConfig.getInstance().getServiceResultBuilder();
        return serviceResultBuilder.buildError(request,response, throwable);
    }
}
