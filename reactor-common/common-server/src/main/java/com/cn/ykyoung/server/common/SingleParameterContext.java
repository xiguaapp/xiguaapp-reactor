/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:57 >
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

package com.cn.ykyoung.server.common;

import com.cn.xiguaapp.common.starter.annotation.Open;
import com.cn.ykyoung.server.bean.ServiceConfig;
import com.cn.ykyoung.server.common.param.SingleParameterClassCreator;
import com.cn.ykyoung.server.common.param.SingleParameterContextValue;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放单个参数
 * @author xiguaapp
 */
public class SingleParameterContext {

    private static SingleParameterClassCreator singleFieldWrapper = new SingleParameterClassCreator();

    private static final Map<String, SingleParameterContextValue> context = new ConcurrentHashMap<>(16);

    /**
     * 添加单个参数
     * @param parameter 接口单个参数
     * @param open open注解
     */
    public static void add(Parameter parameter, Open open) {
        String version = open.version();
        version = "".equals(version) ? ServiceConfig.getInstance().getDefaultVersion() : version;
        String key = open.value() + version;
        String parameterName = parameter.getName();
        Class<?> wrapClass = singleFieldWrapper.create(parameter, parameterName);
        SingleParameterContextValue value = new SingleParameterContextValue();
        value.setParameterName(parameterName);
        value.setWrapClass(wrapClass);
        context.put(key, value);
    }

    public static SingleParameterContextValue get(String name, String version) {
        return context.get(name + version);
    }

    public static void setSingleFieldWrapper(SingleParameterClassCreator singleFieldWrapper) {
        SingleParameterContext.singleFieldWrapper = singleFieldWrapper;
    }
}
