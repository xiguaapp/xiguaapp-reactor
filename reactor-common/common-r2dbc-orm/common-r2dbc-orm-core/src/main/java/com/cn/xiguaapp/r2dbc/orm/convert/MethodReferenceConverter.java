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

package com.cn.xiguaapp.r2dbc.orm.convert;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.wrapper.SerializedLambda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodReferenceConverter {

    private static final Map<Class<?>, String> cache = new ConcurrentHashMap<>();

    public static <T> String convertToColumn(MethodReferenceColumn<T> column) {
        return convertToColumn((Object) column);
    }

    public static <T> String convertToColumn(StaticMethodReferenceColumn<T> column) {
        return convertToColumn((Object) column);
    }

    public static String convertToColumn(Object column) {
        return cache.computeIfAbsent(column.getClass(), t -> {
            SerializedLambda lambda = SerializedLambda.of(column);

            String methodName = lambda.getMethodName();
            if (methodName.startsWith("get")) {
                return StringUtils.toLowerCaseFirstOne(methodName.substring(3));
            } else if (methodName.startsWith("is")) {
                return StringUtils.toLowerCaseFirstOne(methodName.substring(2));
            }
            return methodName;
        });
    }
}
