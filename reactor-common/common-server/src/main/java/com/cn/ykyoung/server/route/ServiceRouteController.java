/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午7:08 >
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

package com.cn.ykyoung.server.route;

import com.cn.ykyoung.server.bean.ServiceApiInfo;
import com.cn.ykyoung.server.utils.OpenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiguaapp
 */
@Slf4j
@RestController
public class ServiceRouteController {

    private static final String SECRET = "a3d9sf!1@odl90zd>fkASwq";

    private static final ApiMetaBuilder apiMetaBuilder = new  ApiMetaBuilder();

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private Environment environment;


    @RequestMapping("/sop/routes")
    public ServiceRouteInfo listRoutes(HttpServletRequest request, HttpServletResponse response) {
        if (!OpenUtil.validateSimpleSign(request, SECRET)) {
            log.error("签名验证失败, params:{}", request.getQueryString());
            return null;
        }
        return getServiceRouteInfo(request, response);
    }

    protected ServiceRouteInfo getServiceRouteInfo(HttpServletRequest request, HttpServletResponse response) {
        String serviceId = environment.getProperty("spring.application.name");
        if (serviceId == null) {
            throw new IllegalArgumentException("未指定spring.application.name参数");
        }
        ServiceApiInfo serviceApiInfo = apiMetaBuilder.getServiceApiInfo(serviceId, requestMappingHandlerMapping);
        ServiceRouteInfoBuilder serviceRouteInfoBuilder = new ServiceRouteInfoBuilder(environment);
        return serviceRouteInfoBuilder.build(serviceApiInfo);
    }

}
