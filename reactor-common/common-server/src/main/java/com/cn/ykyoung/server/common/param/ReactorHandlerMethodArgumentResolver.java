/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:10 >
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

package com.cn.ykyoung.server.common.param;

import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc 解析业务参数接口
 * @since 1.0 17:10
 */
public interface ReactorHandlerMethodArgumentResolver extends HandlerMethodArgumentResolver{
    /**
     * 设置requestMappingHandlerAdapter
     *
     * @param requestMappingHandlerAdapter requestMappingHandlerAdapter
     */
    Mono<Void> setRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter);
}
