/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/5 上午12:43 >
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
import com.cn.common.xiguaapp.test.publish01.MsgBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author xiguaapp
 * @desc 订阅去
 * @since 1.0 00:21
 */
public class SubscribePublish<M> {
    //订阅器名
    private String name;
    //
    final int QUEUE_CAPACITY = 30;
    //订阅者
    private List<ISubcriber> subcribers = new ArrayList<ISubcriber>();
    //订阅器存储队列
    private BlockingQueue<MsgBody> queue = new ArrayBlockingQueue<MsgBody>(QUEUE_CAPACITY);
    public SubscribePublish(String name) {
        this.name = name;
    }

    public void publish(String publisher, M message, boolean isInstantMsgBody) {
        if (isInstantMsgBody) {
            update(publisher, message);
            return;
        }
        MsgBody<M> m = new MsgBody<M>(publisher, message);
        if (!queue.offer(m)) {
            update();
        }
    }

    public void subcribe(ISubcriber subcriber) {
        subcribers.add(subcriber);
    }

    public void unSubcribe(ISubcriber subcriber) {
        subcribers.remove(subcriber);
    }

    public void update() {
        MsgBody m = null;
        while ((m = queue.peek()) != null) {
            this.update(m.getPublisher(), (M) m.getM());
        }
    }

    public void update(String publisher, M MsgBody) {
        for (ISubcriber subcriber : subcribers) {
            subcriber.update(publisher, MsgBody);
        }
    }
}
