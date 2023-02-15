/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:24 >
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

package com.cn.xiguaapp.common.gateway.loadbalance;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.bean.ApiParamAware;
import com.cn.xiguaapp.common.gateway.manage.EnvironmentKeys;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

/**
 * @author xiguaapp
 */
public interface ServerChooserContext<T> extends ApiParamAware<T> {

    /**
     * 通过判断hostname来确定是否是预发布请求
     *
     * @param t t
     * @return 返回true：可以进入到预发环境
     */
    default boolean isRequestFromPreDomain(T t) {
        String domain = EnvironmentKeys.PRE_DOMAIN.getValue();
        if (StringUtils.isEmpty(domain)) {
            return false;
        }
        String[] domains = domain.split("\\,");
        return ArrayUtils.contains(domains, getHost(t));
    }

    default boolean isRequestGrayServer(T t) {
        ApiParam apiParam = getApiParam(t);
        return apiParam.fetchGrayRequest();
    }

    String getHost(T t);
}
