package com.cn.xiguaapp.msg.service.core.subscriber;

import com.cn.xiguaapp.notify.api.core.Notify;
import reactor.core.publisher.Flux;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc
 */
public interface Subscriber {
    Flux<Notify> subscribe();

}
