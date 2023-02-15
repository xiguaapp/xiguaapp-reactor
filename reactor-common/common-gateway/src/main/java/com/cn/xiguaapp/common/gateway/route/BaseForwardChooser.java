/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:13 >
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

package com.cn.xiguaapp.common.gateway.route;

import com.cn.xiguaapp.common.gateway.bean.ApiConfig;
import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.bean.ApiParamAware;
import com.cn.xiguaapp.common.gateway.bean.TargetRoute;
import com.cn.xiguaapp.common.gateway.manage.EnvGrayManager;
import com.cn.xiguaapp.common.gateway.manage.RouteRepositoryContext;
import com.cn.xiguaapp.common.gateway.param.GrayUserBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author xiguaapp
 */
public abstract class BaseForwardChooser<T> implements ForwardChooser<T>, ApiParamAware<T> {

    @Autowired
    private EnvGrayManager envGrayManager;

    @Override
    public ForwardInfo getForwardInfo(T t) {
        ApiParam apiParam = getApiParam(t);
        String nameVersion = apiParam.fetchNameVersion();
        TargetRoute targetRoute = RouteRepositoryContext.getRouteRepository().get(nameVersion);
        String serviceId = targetRoute.getServiceDefinition().fetchServiceIdLowerCase();
        // 如果服务在灰度阶段，返回一个灰度版本号
        String grayVersion = envGrayManager.getVersion(serviceId, nameVersion);
        // 如果是灰度环境
        if (grayVersion != null && isGrayUser(serviceId, apiParam)) {
            String newNameVersion = apiParam.fetchName() + grayVersion;
            TargetRoute targetRouteDest = RouteRepositoryContext.getRouteRepository().get(newNameVersion);
            if (targetRouteDest != null) {
                apiParam.setGrayRequest(true);
                targetRoute = targetRouteDest;
            }
        }
        return new ForwardInfo(targetRoute);
    }

    protected boolean isGrayUser(String serviceId, ApiParam apiParam) {
        List<GrayUserBuilder> grayUserBuilders = ApiConfig.getInstance().getGrayUserBuilders();
        for (GrayUserBuilder grayUserBuilder : grayUserBuilders) {
            String userKey = grayUserBuilder.buildGrayUserKey(apiParam);
            if (envGrayManager.containsKey(serviceId, userKey)) {
                return true;
            }
        }
        return false;
    }

}
