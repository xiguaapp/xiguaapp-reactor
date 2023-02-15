/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/22 下午5:54 >
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

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/22
 * @desc
 */
@Slf4j
public class TopicRouter<T,R> implements Router<T,R> {
    private final Topic<Function<T, Publisher<R>>> root = Topic.createRoot();

    @Override
    public Router<T, R> route(String topic, Function<T, Publisher<R>> handler) {
        root.append(topic).subscribe(handler);
        return this;
    }

    @Override
    public Router<T, R> remove(String topic) {
        root.getTopic(topic).ifPresent(Topic::unsubscribeAll);
        return this;
    }

    @Override
    public Flux<Publisher<R>> execute(String topic, T data) {

        return root.findTopic(topic)
                .flatMapIterable(Topic::getSubscribers)
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    log.debug("not handler for {}", topic);
                }))
                .distinct()
                .map(runner -> runner.apply(data))
                ;
    }

    @Override
    public void close() {
        root.clean();
    }
}
