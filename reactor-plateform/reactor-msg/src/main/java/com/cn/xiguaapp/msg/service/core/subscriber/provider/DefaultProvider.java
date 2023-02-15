/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午8:47 >
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

package com.cn.xiguaapp.msg.service.core.subscriber.provider;

import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import com.cn.xiguapp.common.core.core.config.DefaultConfigMetadata;
import com.cn.xiguapp.common.core.core.event.EventBus;
import com.cn.xiguapp.common.core.gen.AppIdGenerator;
import com.cn.xiguaapp.msg.service.core.subscriber.Subscriber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.cn.xiguaapp.msg.service.core.subscriber.SubscriberProvider;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
@Component
@AllArgsConstructor
public class DefaultProvider implements SubscriberProvider{
    private final EventBus eventBus;
    /**
     * 通知id
     * @return
     */
    @Override
    public String getId(){
        return AppIdGenerator.generatorVersion(3);
    };

    /**
     * 获取名
     * @return
     */
    @Override
    public String getName(){
        return "告警";
    };

    /**
     * 创建
     * @param id
     * @param config
     * @return
     */
    @Override
    public Mono<Subscriber> createSubscriber(String id, Map<String,Object> config){
//        eventBus.subscribe(
//                Subscription.of(getName() + id
//                        , String.format("/ractor/advice/%s", config)
//                        , Subscription.Feature.local))
//                .map(msg->{
//                    JSONObject jsonObject = msg.bodyToJson();
//                    return Notify.of(String.format("服务[%s]发生告警:[%s]",jsonObject.get("")))
//                })
//                ;
        return null;
    };

    /**
     * 获取配置文件
     * @return
     */
    @Override
    public ConfigMetadata getConfigMetadate(){
        //到时候自定义
        return new DefaultConfigMetadata()
                .add("productId","产品id","产品id,支持：*")
                .add("deviceId","设备id","设备id，支持:*")
                .add("productId","告警id","告警id,支持通配符:id");
    };

}
