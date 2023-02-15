/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午10:35 >
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

/**
 * 路由状态
 *
 * @author xiguaapp
 */
public enum RouteStatus {
    /**
     * 路由状态，0：待审核
     */
    AUDIT(0, "待审核"),
    /**
     * 路由状态，1：已启用
     */
    ENABLE(1, "已启用"),
    /**
     * 路由状态，2：已禁用
     */
    DISABLE(2, "已禁用"),
    ;
    private byte status;
    private String description;

    RouteStatus(Integer status, String description) {
        this.status = status.byteValue();
        this.description = description;
    }

    public byte getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }}
