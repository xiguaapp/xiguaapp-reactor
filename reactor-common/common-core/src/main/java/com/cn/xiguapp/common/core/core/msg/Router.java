/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/22 下午5:51 >
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

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/22
 * @desc 基于主题的路由器
 */
public interface Router<K,V> {
    /**
     * 拼接-创建
     * @param topic
     * @param handler
     * @return
     */
    Router<K,V>route(String topic, Function<K, Publisher<V>>handler);

    /**
     * 移除
     * @param topic
     * @return
     */
    Router<K,V>remove(String topic);

    /**
     * 执行
     * @param topic
     * @param data
     * @return
     */
    Flux<Publisher<V>>execute(String topic,K data);

    /**
     * 关闭/清空
     */
    void close();

    /**
     * 创建
     * @param <K>
     * @param <V>
     * @return
     */
    static <K, V> Router<K, V> create() {
        return new TopicRouter<>();
    }
}
