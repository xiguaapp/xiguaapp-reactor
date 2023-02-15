/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午8:46 >
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

package com.cn.xiguaapp.msg.service.core.subscriber;

import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
public interface SubscriberProvider {
    /**
     * 获取数据项id
     * @return
     */
    String getId();

    /**
     * 获取名
     * @return
     */
    String getName();

    /**
     * 创建
     * @param id
     * @param config
     * @return
     */
    Mono<Subscriber> createSubscriber(String id, Map<String,Object>config);

    /**
     * 获取配置文件
     * @return
     */
    ConfigMetadata getConfigMetadate();
}
