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

import com.cn.xiguapp.common.core.core.msg.Payload;
import lombok.NonNull;

import java.util.Objects;

/**
 * @author xiguaapp
 * @package_name xiguaapp-reactor
 * @Date 2020/11/21
 * @desc 解码器
 */
public interface Decoder<T> {
    /**
     * 对象类型
     * @return
     */
    Class<T> forType();

    /**
     * 解码
     * @param payload
     * @return
     */
    T decode(@NonNull Payload payload);

    /**
     * 判断对象是否为已知对象
     * @param obj
     * @return
     */
    default boolean isDecodeFrom(Object obj){
        return !Objects.isNull(obj) && forType().isInstance(obj);
    }
}
