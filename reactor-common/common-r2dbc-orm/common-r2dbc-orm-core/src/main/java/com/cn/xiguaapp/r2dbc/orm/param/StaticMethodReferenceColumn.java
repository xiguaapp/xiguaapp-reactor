/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.r2dbc.orm.param;

import com.cn.xiguaapp.r2dbc.orm.convert.MethodReferenceConverter;
import com.cn.xiguaapp.r2dbc.orm.properties.Conditional;
import com.cn.xiguaapp.r2dbc.orm.properties.NestConditional;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 使用静态方法引用来定义列名,如:
 * <pre>
 *     createQuery().where(UserEntity::getName,"name").single();
 * </pre>
 *
 * @param <T>
 * @see Conditional
 * @see NestConditional
 * @see MethodReferenceColumn
 */
public interface StaticMethodReferenceColumn<T> extends Function<T, Object>, Serializable {

    default String getColumn() {
        return MethodReferenceConverter.convertToColumn(this);
    }
}
