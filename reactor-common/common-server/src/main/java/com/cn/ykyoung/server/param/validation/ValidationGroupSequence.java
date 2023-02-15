/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:23 >
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

package com.cn.ykyoung.server.param.validation;

import javax.validation.GroupSequence;

/**
 * @author xiguaapp
 */
@GroupSequence({
        // 默认的必须加上，不然没有指定groups的注解不会生效
        javax.validation.groups.Default.class,
        Group1.class,
        Group2.class,
        Group3.class,
        Group4.class,
        Group5.class,

})
public interface ValidationGroupSequence {
}

