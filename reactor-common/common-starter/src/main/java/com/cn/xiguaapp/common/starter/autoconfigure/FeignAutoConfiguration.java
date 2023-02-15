/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.common.starter.autoconfigure;

import com.cn.xiguapp.common.core.exception.ApiException;
import com.cn.xiguapp.common.core.exception.ServiceResultBuilder;
import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.common.starter.common.FeignRequestInterceptor;
import feign.*;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xiguaapp
 * @desc Feign OAuth2 request interceptor.
 */
@Slf4j
@Configuration
public class FeignAutoConfiguration {
    public static int connectTimeOutMillis = 10;
    public static int readTimeOutMillis = 10;

    @Bean
    public Encoder feignSpringFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        Encoder encoder = new FeignSpringFormEncoder(new SpringEncoder(messageConverters));
        log.info("FeignSpringFormEncoder [{}]", encoder);
        return encoder;
    }
    @Bean
    @ConditionalOnMissingBean(FeignRequestInterceptor.class)
    public RequestInterceptor feignRequestInterceptor() {
        FeignRequestInterceptor interceptor = new FeignRequestInterceptor();
        log.info("FeignRequestInterceptor [{}]", interceptor);
        return interceptor;
    }

    /**
     * 配置此项可以让feign接口支持reqestLine("GET /xx/xx/xx/xx/xx")这种东西
     * @return
     */
    @Bean
    public Contract feignContact(){
        return new Contract.Default();
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, TimeUnit.SECONDS, readTimeOutMillis, TimeUnit.SECONDS,true);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100,1000,2);
    }
    @Bean
    public ErrorDecoder errorDecoder(){
        ErrorDecoder errorDecoder = new FeignErrorDecoder();
        log.info("FeignErrorDecoder [{}]",errorDecoder);
        return errorDecoder;
    }


    /**
     * 当调用服务时 如果服务返回状态码不为200 就会进入到Feign的ErrorDecoder中，因此如果我们要解析异常信息，就要重写ErrorDecoder
     */
    public static class FeignErrorDecoder implements ErrorDecoder{
        @Autowired(required = false)
        ServiceResultBuilder serviceResultBuilder;

        @SneakyThrows
        @Override
        public Exception decode(String methodKey, Response response) {
            //自定义接口
            String s = Util.toString(response.body().asReader());
            if (StringUtils.isBlank(s)){
                return null;
            }

            log.info("feign异常{}",response.body());
            return new ApiException(String.valueOf(response.body()));
        }
    }

    /**
     * @desc : 处理多个文件上传
     * @author: xiguaapp
     * @date 2020/12/16 下午4:26
     */
    public static class FeignSpringFormEncoder extends SpringFormEncoder {
        public FeignSpringFormEncoder(Encoder delegate) {
            super(delegate);
            MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
            processor.addWriter(new SpringSingleMultipartFileWriter());
            processor.addWriter(new SpringManyMultipartFilesWriter());
        }

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            if (bodyType != null && bodyType.equals(MultipartFile[].class) && object != null) {
                MultipartFile[] file = (MultipartFile[]) object;
                if (file.length == 0) {
                    return;
                }
                Map data = Collections.singletonMap(file[0].getName(), object);
                super.encode(data, MAP_STRING_WILDCARD, template);
                return;
            }
            super.encode(object, bodyType, template);
        }
    }
}
