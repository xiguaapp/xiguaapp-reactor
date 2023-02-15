/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:52 >
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

package com.cn.xiguapp.common.core.core.msg;

import com.cn.xiguapp.common.core.utils.EnumDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Subscription implements Serializable {
    private static final long serialVersionUID = -6849794470754667710L;

    public static final Feature[] DEFAULT_FEATURES = Subscription.Feature.values();

    //订阅者标识
    private final String subscriber;

    //订阅主题,主题以/分割,如: /device/TS-01/09012/message 支持通配符 /device/**
    private final String[] topics;

    //订阅特性
    private final Feature[] features;

    private Runnable doOnSubscribe;

    public static Subscription of(String subscriber, String... topic) {
        return new Subscription(subscriber, topic, DEFAULT_FEATURES, null);
    }

    public static Subscription of(String subscriber, String[] topic, Feature... features) {
        return new Subscription(subscriber, topic, features, null);
    }

    public static Subscription of(String subscriber, String topic, Feature... features) {
        return new Subscription(subscriber, new String[]{topic}, features, null);
    }

    public Subscription copy(Feature... newFeatures) {
        return new Subscription(subscriber, topics, newFeatures, null);
    }

    public Subscription onSubscribe(Runnable sub) {
        this.doOnSubscribe = sub;
        return this;
    }

    public boolean hasFeature(Subscription.Feature feature) {
        return feature.in(this.features);
    }

    @AllArgsConstructor
    @Getter
    public enum Feature implements EnumDict<String> {

        //如果相同的订阅者,只有一个订阅者收到消息
        shared("shared"),
        //订阅本地消息
        local("订阅本地消息"),
        //订阅来自代理的消息
        broker("订阅代理消息");

        private final String text;

        @Override
        public String getValue() {
            return name();
        }
    }
}
