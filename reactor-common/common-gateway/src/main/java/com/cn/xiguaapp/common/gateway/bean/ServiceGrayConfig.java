/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:17 >
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

package com.cn.xiguaapp.common.gateway.bean;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author xiguaapp
 */
@Data
public class ServiceGrayConfig {

    private String serviceId;

    /**
     * 用户id
     */
    private Set<String> userKeys;

    /** 存放接口隐射关系，key:nameversion，value:newVersion */
    private Map<String, String> grayNameVersion;

    public boolean containsKey(Object userKey) {
        return userKeys.contains(String.valueOf(userKey));
    }

    public String getVersion(String name) {
        return grayNameVersion.get(name);
    }

}
