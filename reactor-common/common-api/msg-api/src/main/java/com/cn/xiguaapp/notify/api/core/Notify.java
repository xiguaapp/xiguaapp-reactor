/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/22 下午4:59 >
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

package com.cn.xiguaapp.notify.api.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc
 */
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@NoArgsConstructor
public class Notify {
    //消息
    private String message;
    //数据项id
    private String dataId;
    //通知时间
    private long notifyTime;
}
