/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:16 >
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

package com.cn.xiguaapp.common.gateway.manage;


import com.cn.xiguaapp.common.gateway.bean.BeanInitializer;
import com.cn.xiguaapp.common.gateway.bean.ServiceGrayConfig;

/**
 * @author xiguaapp
 */
public interface EnvGrayManager extends BeanInitializer {

    /**
     * 保存灰度配置
     * @param serviceGrayConfig 灰度配置
     */
    void saveServiceGrayConfig(ServiceGrayConfig serviceGrayConfig);

    /**
     * 实例是否允许
     * @param serviceId serviceId
     * @param userKey 用户key，如appKey
     * @return true：允许访问
     */
    boolean containsKey(String serviceId, Object userKey);

    /**
     * 获取灰度发布新版本号
     * @param serviceId serviceId
     * @param nameVersion 路由id
     * @return 返回新版本号
     */
    String getVersion(String serviceId, String nameVersion);

    /**
     * 开启灰度
     * @param instanceId instanceId
     * @param serviceId serviceId
     */
    void openGray(String instanceId, String serviceId);

    /**
     * 关闭灰度
     * @param instanceId instanceId
     */
    void closeGray(String instanceId);
}
