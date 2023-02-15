/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:55 >
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

import com.cn.xiguapp.common.core.core.codec.Codec;
import com.cn.xiguapp.common.core.core.msg.Payload;
import com.cn.xiguapp.common.core.core.msg.TopicPayload;
import com.cn.xiguapp.common.core.utils.BytesUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
public class TopicPayloadCodec implements Codec<TopicPayload> {
    public static final TopicPayloadCodec INSTANCE = new TopicPayloadCodec();

    @Override
    public Class<TopicPayload> forType() {
        return TopicPayload.class;
    }

    @Nullable
    @Override
    public TopicPayload decode(@NonNull Payload payload) {

        ByteBuf byteBuf = payload.getBody();

        byte[] topicLen = new byte[4];

        byteBuf.getBytes(0, topicLen);
        int bytes = BytesUtils.beToInt(topicLen);

        byte[] topicBytes = new byte[bytes];

        byteBuf.getBytes(4, topicBytes);
        String topic = new String(topicBytes);

        int idx = 4 + bytes;

        ByteBuf body = byteBuf.slice(idx, byteBuf.readableBytes() - idx);
        byteBuf.resetReaderIndex();
        return TopicPayload.of(topic, Payload.of(body));
    }

    @Override
    public Payload encode(TopicPayload body) {

        byte[] topic = body.getTopic().getBytes();
        byte[] topicLen = BytesUtils.intToBe(topic.length);

        return Payload.of(ByteBufAllocator.DEFAULT.compositeBuffer(3)
                .addComponent(true, Unpooled.wrappedBuffer(topicLen))
                .addComponent(true, Unpooled.wrappedBuffer(topic))
                .addComponent(true, body.getBody()));

    }
}
