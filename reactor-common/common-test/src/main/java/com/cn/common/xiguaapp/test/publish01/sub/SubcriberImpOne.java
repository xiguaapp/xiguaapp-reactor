/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/5 上午12:30 >
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

package com.cn.common.xiguaapp.test.publish01.sub;

import com.cn.common.xiguaapp.test.publish01.ISubcriber;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 00:30
 */
public class SubcriberImpOne <M> implements ISubcriber<M> {
    public String name;

    public SubcriberImpOne(String name) {
        super();
        this.name = name;
    }

    @Override
    public void subcribe(SubscribePublish subscribePublish) {
        subscribePublish.subcribe(this);
    }

    @Override
    public void unSubcribe(SubscribePublish subscribePublish) {
        subscribePublish.unSubcribe(this);
    }

    @Override
    public void update(String publisher, M message) {
        System.out.println(this.name + "收到" + publisher + "发来的消息:" + message.toString());
    }
}
