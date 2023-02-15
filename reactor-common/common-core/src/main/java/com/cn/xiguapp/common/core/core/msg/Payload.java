/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午4:39 >
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.xiguapp.common.core.core.codec.Codecs;
import com.cn.xiguapp.common.core.core.codec.Decoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc 消息负载接口
 */
public interface Payload {
    @NonNull ByteBuf getBody();
    default <T> T decode(Decoder<T> decoder, boolean release) {
        try {
            return decoder.decode(this);
        } finally {
            if (release) {
                release();
            }
        }
    }

    default <T> T decode(Decoder<T> decoder) {
        return decode(decoder, false);
    }

    default <T> T decode(Class<T> decoder) {
        return decode(Codecs.lookup(decoder), false);
    }

    default Object decode() {
        byte[] payload = getBytes();
        //maybe json
        if (/* { }*/(payload[0] == 123 && payload[payload.length - 1] == 125)
                || /* [ ] */(payload[0] == 91 && payload[payload.length - 1] == 93)
        ) {
            return JSON.parse(new String(payload));
        }
        return decode(Object.class);
    }

    default <T> T convert(Function<ByteBuf, T> mapper) {
        return convert(mapper, false);
    }

    default <T> T convert(Function<ByteBuf, T> mapper, boolean release) {
        ByteBuf body = getBody();
        try {
            return mapper.apply(body);
        } finally {
            if (release) {
                release();
            }
        }
    }

    default void retain() {
        getBody().retain();
    }

    default void retain(int inc) {
        getBody().retain(inc);
    }

    default void release(int dec) {
        getBody().release(dec);
    }

    default void release() {
        getBody().release();
    }

    default byte[] getBytes() {
        return getBytes(false);
    }

    default byte[] getBytes(boolean release) {
        return convert(ByteBufUtil::getBytes, release);
    }

    default byte[] getBytes(int offset, int length, boolean release) {
        return convert(byteBuf -> ByteBufUtil.getBytes(byteBuf, offset, length), release);
    }

    default String bodyToString() {
        return getBody().toString(StandardCharsets.UTF_8);
    }

    default JSONObject bodyToJson() {
        return decode(JSONObject.class);
    }

    default JSONArray bodyToJsonArray() {
        return decode(JSONArray.class);
    }

    Payload voidPayload = () -> Unpooled.EMPTY_BUFFER;

    static Payload of(ByteBuf body) {
        return () -> body;
    }

    static Payload of(byte[] body) {
        return of(Unpooled.wrappedBuffer(body));
    }

    static Payload of(String body) {
        return of(body.getBytes());
    }

}
