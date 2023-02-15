/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:36 >
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

package com.cn.xiguapp.common.core.core.codec.defult;

import com.alibaba.fastjson.JSON;
import com.cn.xiguapp.common.core.core.codec.Codec;
import com.cn.xiguapp.common.core.core.msg.Payload;
import io.netty.buffer.Unpooled;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
public class JsonArrayCodec<T, R> implements Codec<R> {
    private final Class<T> type;

    private final Class<R> resultType;

    private final Function<List<T>, R> mapper;

    private JsonArrayCodec(Class<T> type, Class<R> resultType, Function<List<T>, R> mapper) {
        this.type = type;
        this.resultType = resultType;
        this.mapper = mapper;
    }

    @SuppressWarnings("all")
    public static <T> JsonArrayCodec<T, List<T>> of(Class<T> type) {
        return JsonArrayCodec.of(type,(Class) List.class, Function.identity());
    }

    public static <T, R> JsonArrayCodec<T, R> of(Class<T> type, Class<R> resultType, Function<List<T>, R> function) {
        return new JsonArrayCodec<>(type, resultType,function);
    }

    @Override
    public Class<R> forType() {
        return resultType;
    }

    @Override
    public R decode(@NonNull Payload payload) {
        return mapper.apply(JSON.parseArray(payload.bodyToString(), type));
    }

    @Override
    public Payload encode(R body) {
        return () -> Unpooled.wrappedBuffer(JSON.toJSONBytes(body));
    }


}
