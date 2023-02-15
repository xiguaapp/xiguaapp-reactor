/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:52 >
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.xiguapp.common.core.core.codec.Decoder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;

/**
 * @author xiguaapp
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TopicPayload implements Payload {

    private String topic;

    private Payload payload;

    @NonNull
    @Override
    public ByteBuf getBody() {
        return payload.getBody();
    }

    @Override
    public void release() {
        payload.release();
    }

    @Override
    public void release(int dec) {
        payload.release();
    }

    @Override
    public void retain() {
        payload.retain();
    }

    @Override
    public void retain(int inc) {
        payload.retain(inc);
    }

    @Override
    public String toString() {
        return "{" +
                "topic='" + topic + '\'' +
                ", payload=" + payload +
                '}';
    }

    @Override
    public JSONObject bodyToJson() {
        return payload.bodyToJson();
    }

    @Override
    public JSONArray bodyToJsonArray() {
        return payload.bodyToJsonArray();
    }

    @Override
    public String bodyToString() {
        return payload.bodyToString();
    }

    @Override
    public Object decode() {
        return payload.decode();
    }

    @Override
    public <T> T decode(Class<T> decoder) {
        return payload.decode(decoder);
    }

    @Override
    public <T> T decode(Decoder<T> decoder) {
        return payload.decode(decoder);
    }

    @Override
    public <T> T decode(Decoder<T> decoder, boolean release) {
        return payload.decode(decoder, release);
    }

    public Map<String, String> getTopicVars(String pattern) {
        return TopicUtils.getPathVariables(pattern, getTopic());
    }
}
