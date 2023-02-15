/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 下午7:40 >
 *
 *     Send: 1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.sba.server.notifier;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguapp.common.core.utils.DingtalkSign;
import com.cn.xiguaapp.sba.server.configuration.DingTalkMarkdownMessage;
import com.cn.xiguaapp.sba.server.utils.DingtailkMessage;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Slf4j
public class DingNotifier extends AbstractStatusChangeNotifier {
    public DingNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        log.info("*****进入发送消息模式****");
        long timeMillis = System.currentTimeMillis();
        String serviceName = instance.getRegistration().getName();
        String serviceUrl = instance.getRegistration().getServiceUrl();
        String status = instance.getStatusInfo().getStatus();
        Map<String, Object> details = instance.getStatusInfo().getDetails();
        String str = String.format("服务预警 \n> 服务名:%s \n 【服务地址】:%s \n【状态】:%s \n 【详情】:%s \n![screenshot](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTK9kuZIx9MaKWX5ewUy6wuPKKzneMlNFV1dQ&usqp=CAU)\n> ###### %s [服务地址](http://localhost:4344)"
                ,serviceName,serviceUrl,status, JSONObject.toJSONString(details),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh:mm")));
        return Mono.fromRunnable(() -> {
            DingtailkMessage.sendDingTalkMessage( "10145bb312db0d617144d599a2432ee9dbbb4641031da9bfb837b1645677203c"
                    ,timeMillis
                    ,DingtalkSign.sign(timeMillis)
                    , DingTalkMarkdownMessage.builder()
                            .text(str)
                            .isAtAll(true)
                            .title("服务预警")
                            .build());
        });
    }
}