/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午5:38 >
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
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc 布尔类型
 */
@NoArgsConstructor
public class BooleanCodec implements Codec<Boolean> {
    public static BooleanCodec INSTANCE = new BooleanCodec();
    @Override
    public Class<Boolean> forType() {
        return Boolean.class;
    }

    @Override
    public Boolean decode(@NonNull Payload payload) {
        byte[] bytes = payload.getBytes();
        return bytes.length>0&&bytes[0]>0;
    }

    @Override
    public Payload encode(Boolean content) {
        return ()-> Unpooled.wrappedBuffer(new byte[]{content?(byte)1:0});
    }
}
