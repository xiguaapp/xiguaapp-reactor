/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/5 上午12:31 >
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

import com.cn.common.xiguaapp.test.publish01.sub.PublisherImpOne;
import com.cn.common.xiguaapp.test.publish01.sub.SubcriberImpOne;
import com.cn.common.xiguaapp.test.publish01.sub.SubscribePublish;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 00:31
 */
public class SubPub {
    public static void main(String[] args) {
        SubscribePublish<String> subscribePublish = new SubscribePublish<String>("订阅调度哥");
        IPublisher<String> publisher1 = new PublisherImpOne<String>("【发布者老哥1】");
        ISubcriber<String> subcriber1 = new SubcriberImpOne<String>("【订阅者老兄】");
        ISubcriber<String> subcriber2 = new SubcriberImpOne<String>("【订阅者老弟】");
        subcriber1.subcribe(subscribePublish);
        subcriber2.subcribe(subscribePublish);
        publisher1.publish(subscribePublish, "发布第一条", true);
        publisher1.publish(subscribePublish, "发布第二条消息", true);
        publisher1.publish(subscribePublish, "yy", false);
    }
}
