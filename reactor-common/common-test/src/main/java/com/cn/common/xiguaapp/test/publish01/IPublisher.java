/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/5 上午12:19 >
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

package com.cn.common.xiguaapp.test.publish01;

import com.cn.common.xiguaapp.test.publish01.sub.SubscribePublish;

/**
 * @author xiguaapp
 * @desc 发布者接口
 * @since 1.0 00:19
 */
public interface IPublisher<M> {
    void publish(SubscribePublish subscribePublish, M message, boolean isInstantMsg);
}
