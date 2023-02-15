/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/22 下午5:47 >
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

package com.cn.xiguaapp.msg.service.core.manage;

import com.cn.xiguapp.common.core.core.codec.Codecs;
import com.cn.xiguapp.common.core.core.codec.Decoder;
import com.cn.xiguapp.common.core.core.codec.Encoder;
import com.cn.xiguapp.common.core.core.event.EventBus;
import com.cn.xiguapp.common.core.core.msg.Subscription;
import com.cn.xiguapp.common.core.core.msg.TopicPayload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/11/3
 * @desc 默认:支持事件代理的事件总线，可以通过该代理方式来实现集群和分布式事件总线任务
 */
@Service
@Slf4j
@AllArgsConstructor
public class DefaultEventBus implements EventBus {
    /**
     * 从事件总线中订阅事件
     * @param subscription 订阅信息
     * @return
     */
    @Override
    public Flux<TopicPayload> subscribe(Subscription subscription) {
        return null;
    }

    /**
     * 从事件总线中订阅事件,并按照指定的解码器进行数据转换
     * @param subscription 订阅信息
     * @param decoder      解码器
     * @param <T>
     * @return
     */
    @Override
    public <T> Flux<T> subscribe(Subscription subscription, Decoder<T> decoder) {
        return subscribe(subscription).flatMap(payload->{
            try {
                payload.retain();
                return Mono.justOrEmpty(payload.decode(decoder));
            }finally {
                payload.release();
            }
        });
    }

    /**
     * 推送消息流到事件总线,并返回有多少订阅者订阅了此topic,默认自动根据元素类型进行序列化
     * @param topic topic
     * @param event 事件流
     * @param <T>
     * @return
     */
    @Override
    public <T> Mono<Long> publish(String topic, Publisher<T> event) {
        return publish(topic, (Encoder<T>) msg -> Codecs.lookup((Class<T>) msg.getClass()).encode(msg), event);
    }

    /**
     * 推送消息流，并指定编码器用于进行事件序列化
     * @param topic       topic
     * @param encoder     编码器
     * @param eventStream 事件流
     * @param <T>
     * @return
     */
    @Override
    public <T> Mono<Long> publish(String topic, Encoder<T> encoder, Publisher<? extends T> eventStream) {
        return null;

    }
}
