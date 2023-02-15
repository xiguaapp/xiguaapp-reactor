/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:56 >
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
import com.cn.xiguapp.common.core.core.msg.Subscription;
import com.cn.xiguapp.common.core.utils.BytesUtils;
import com.cn.xiguapp.common.core.utils.EnumDict;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
public class SubscriptionCodec implements Codec<Subscription> {
    public static final SubscriptionCodec INSTANCE = new SubscriptionCodec();

    @Override
    public Class<Subscription> forType() {
        return Subscription.class;
    }

    @Nullable
    @Override
    public Subscription decode(@NonNull Payload payload) {

        ByteBuf body = payload.getBody();

        byte[] subscriberLenArr = new byte[4];
        body.getBytes(0, subscriberLenArr);
        int subscriberLen = BytesUtils.beToInt(subscriberLenArr);

        byte[] subscriber = new byte[subscriberLen];
        body.getBytes(4, subscriber);
        String subscriberStr = new String(subscriber);

        byte[] featureBytes = new byte[8];
        body.getBytes(4 + subscriberLen, featureBytes);
        Subscription.Feature[] features = EnumDict
                .getByMask(Subscription.Feature.class, BytesUtils.beToLong(featureBytes))
                .toArray(new Subscription.Feature[0]);

        int headerLen = 12 + subscriberLen;
        body.resetReaderIndex();
        return Subscription.of(subscriberStr, body.slice(headerLen, body.readableBytes() - headerLen)
                .toString(StandardCharsets.UTF_8)
                .split("[\t]"), features);
    }

    @Override
    public Payload encode(Subscription body) {

        // bytes 4
        byte[] subscriber = body.getSubscriber().getBytes();
        byte[] subscriberLen = BytesUtils.intToBe(subscriber.length);

        // bytes 8
        long features = EnumDict.toMask(body.getFeatures());
        byte[] featureBytes = BytesUtils.longToBe(features);

        byte[] topics = String.join("\t", body.getTopics()).getBytes();

        return Payload.of(Unpooled
                .buffer(subscriberLen.length + subscriber.length + featureBytes.length + topics.length)
                .writeBytes(subscriberLen)
                .writeBytes(subscriber)
                .writeBytes(featureBytes)
                .writeBytes(topics));
    }
}
