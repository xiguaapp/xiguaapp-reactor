/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:42 >
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
import lombok.NonNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc
 */
public class StringCodec implements Codec<String> {
    public static StringCodec DEFAULT = of(Charset.defaultCharset());

    public static StringCodec UTF8 = of(StandardCharsets.UTF_8);

    public static StringCodec ASCII = of(StandardCharsets.US_ASCII);

    public static StringCodec of(Charset charset) {
        return new StringCodec(charset);
    }

    private final Charset charset;

    private StringCodec(Charset charset) {
        this.charset = charset;
    }

    @Override
    public Class<String> forType() {
        return String.class;
    }

    @Override
    public String decode(@NonNull Payload payload) {

        return payload.getBody().toString(charset);
    }

    @Override
    public Payload encode(String body) {
        return () -> Unpooled.wrappedBuffer(body.getBytes(charset));
    }

}
