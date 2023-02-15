/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:59 >
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

package com.cn.xiguapp.common.core.core.config;

import java.util.Map;
import java.util.Optional;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/30
 * @desc
 */
public interface MetaData {
    String getId();

    String getName();

    String getDescription();

    Map<String, Object> getExpands();

    default Optional<Object> getExpand(String name) {
        return Optional.ofNullable(getExpands())
                .map(map -> map.get(name));
    }

    default void setExpands(Map<String, Object> expands) {
    }

    default void setName(String name) {

    }

    default void setDescription(String description) {

    }
}
