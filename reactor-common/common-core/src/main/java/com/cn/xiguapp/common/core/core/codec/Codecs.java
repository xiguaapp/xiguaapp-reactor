/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午4:38 >
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

package com.cn.xiguapp.common.core.core.codec;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc 解码器
 */
@SuppressWarnings("all")
@Slf4j
public final class Codecs {
    private static Map<ResolvableType,Codec<?>>mapping = new ConcurrentHashMap<>();

    private static List<CodecsSupport>allCodec = new CopyOnWriteArrayList<>();
    //初始化
    static {
        ServiceLoader.load(CodecsSupport.class)
                .forEach(allCodec::add);
        allCodec.sort(Comparator.comparingInt(CodecsSupport::getOrder));
    }

    /**
     * 注册
     */
    public static final Consumer<CodecsSupport>register=support-> {
        allCodec.add(support);
        allCodec.sort(Comparator.comparingInt(CodecsSupport::getOrder));
    };
    @NonNull
    private static Codec<?> resolve(ResolvableType type){
        for (CodecsSupport support : allCodec) {
            Optional<Codec<?>> lookup = (Optional) support.lookup(type);

            if (lookup.isPresent()) {
                log.debug("lookup codec [{}] for [{}]", lookup.get(), type);
                return lookup.get();
            }
        }
        throw new UnsupportedOperationException("unsupported codec for " + type);
    }

    public static <T> Codec<T> lookup(@NonNull Class<? extends T> target) {
        return lookup(ResolvableType.forType(target));
    }

    public static <T> Codec<T> lookup(ResolvableType type) {
        if (Publisher.class.isAssignableFrom(type.toClass())) {
            type = type.getGeneric(0);
        }
        return (Codec<T>) mapping.computeIfAbsent(type,Codecs::resolve);
    }
}
