package com.cn.xiguaapp.datasource.api.switcher;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 */
public interface ReactiveSwitcher {

    <P extends Publisher<?>> P useLast(P publisher);

    <P extends Publisher<?>> P use(P publisher, String id);

    <P extends Publisher<?>> P useDefault(P publisher);

    Mono<String> current();

    <P extends Publisher<?>> P reset(P publisher);

}
