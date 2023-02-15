/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午8:34 >
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

package com.cn.xiguaapp.msg.service.core.function;

import com.cn.xiguapp.common.core.core.event.EventBus;
import com.cn.xiguaapp.msg.service.core.subscriber.SubscriberProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc 消息订阅者
 */
@Component
@Slf4j
public class NotifySubscriberFunction implements CommandLineRunner {
    private final EventBus eventBus;
    private final Map<String, Disposable> subscribers = new ConcurrentHashMap<>();
    private final Map<String,SubscriberProvider> providers = new ConcurrentHashMap<>();

    public NotifySubscriberFunction(EventBus eventBus, List<SubscriberProvider> providers) {
        this.eventBus = eventBus;
        for (SubscriberProvider provider : providers) {
            this.providers.put(provider.getId(), provider);
        }
    }

    public Optional<SubscriberProvider>getProvier(String provier){
        return Optional.ofNullable(provier).map(providers::get);
    }



    @Override
    public void run(String... args) throws Exception {

    }
}
