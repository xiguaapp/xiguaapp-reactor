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

package com.cn.xiguaapp.r2dbc.orm.config;


import com.cn.xiguaapp.r2dbc.orm.convert.ObjectConverter;
import com.cn.xiguaapp.r2dbc.orm.operator.ObjectPropertyOperator;
import com.cn.xiguaapp.r2dbc.orm.properties.ApacheCommonPropertyOperator;

public class GlobalConfig {

    private static ObjectPropertyOperator propertyOperator;

    static {
        setPropertyOperator(ApacheCommonPropertyOperator.INSTANCE);
        setObjectConverter(ApacheCommonPropertyOperator.INSTANCE);
    }

    private static ObjectConverter objectConverter;

    public static ObjectConverter getObjectConverter() {
        return objectConverter;
    }

    public static ObjectPropertyOperator getPropertyOperator() {
        return propertyOperator;
    }

    public static void setPropertyOperator(ObjectPropertyOperator propertyOperator) {
        GlobalConfig.propertyOperator = propertyOperator;
    }

    public static void setObjectConverter(ObjectConverter objectConverter) {
        GlobalConfig.objectConverter = objectConverter;
    }
}
