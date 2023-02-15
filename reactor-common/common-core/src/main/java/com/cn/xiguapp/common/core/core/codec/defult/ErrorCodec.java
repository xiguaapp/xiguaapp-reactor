/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:03 >
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
import io.netty.buffer.Unpooled;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodec implements Codec<Throwable> {
    public static ErrorCodec RUNTIME = of(RuntimeException::new);


    public static ErrorCodec DEFAULT = RUNTIME;


    public static ErrorCodec of(Function<String, Throwable> mapping) {
        return new ErrorCodec(mapping);
    }

    Function<String, Throwable> mapping;

    @Override
    public Class<Throwable> forType() {
        return Throwable.class;
    }

    @Override
    public Throwable decode(@NonNull Payload payload) {
        return mapping.apply(payload.bodyToString());
    }

    @Override
    public Payload encode(Throwable body) {
        String message = body.getMessage() == null ? body.getClass().getSimpleName() : body.getMessage();
        return () -> Unpooled.wrappedBuffer(message.getBytes());
    }
}
