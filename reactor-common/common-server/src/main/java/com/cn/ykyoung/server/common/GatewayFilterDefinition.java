/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:06 >
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

package com.cn.ykyoung.server.common;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiguaapp
 */
@Data
public class GatewayFilterDefinition {
    /** Filter Name */
    private String name;
    /** 对应的路由规则 */
    private Map<String, String> args = new LinkedHashMap<>();
}