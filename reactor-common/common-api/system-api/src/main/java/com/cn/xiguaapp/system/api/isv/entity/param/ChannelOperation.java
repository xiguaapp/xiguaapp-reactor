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

package com.cn.xiguaapp.system.api.isv.entity.param;

/**
 * @author xiguaapp
 */
public enum ChannelOperation {
    /**
     * 限流推送路由配置-修改
     */
    LIMIT_CONFIG_UPDATE("update"),

    /**
     * 路由信息更新
     */
    ROUTE_CONFIG_UPDATE("update"),

    /**
     * isv信息修改
     */
    ISV_INFO_UPDATE("update"),

    /**
     * 黑名单消息类型：添加
     */
    BLACKLIST_ADD("add"),
    /**
     * 黑名单消息类型：删除
     */
    BLACKLIST_DELETE("delete"),

    /**
     * 路由权限配置更新
     */
    ROUTE_PERMISSION_UPDATE("update"),
    /**
     * 路由权限加载
     */
    ROUTE_PERMISSION_RELOAD("reload"),

    /**
     * 灰度发布设置
     */
    GRAY_USER_KEY_SET("set"),
    /**
     * 灰度发布-开启
     */
    GRAY_USER_KEY_OPEN("open"),
    /**
     * 灰度发布-关闭
     */
    GRAY_USER_KEY_CLOSE("close"),

    ;

    private String operation;

    ChannelOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
