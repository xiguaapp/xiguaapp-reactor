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

package com.cn.xiguaapp.r2dbc.orm.properties;

import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguaapp.r2dbc.orm.convert.ObjectConverter;
import com.cn.xiguaapp.r2dbc.orm.operator.ObjectPropertyOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApacheCommonPropertyOperator implements ObjectPropertyOperator, ObjectConverter {

    private static PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();

    public static final ApacheCommonPropertyOperator INSTANCE = new ApacheCommonPropertyOperator();

    @Override
    public Optional<Object> getProperty(Object object, String name) {
        try {
            return Optional.ofNullable(propertyUtils.getProperty(object, name));
        } catch (NoSuchMethodException ignore) {

        } catch (Exception e) {
            log.info("无法获取属性:{},对象:{}", name, object, e);
        }
        return Optional.empty();
    }

    @Override
    @SneakyThrows
    public void setProperty(Object object, String name, Object value) {
        try {
            BeanUtils.setProperty(object, name, value);
        } catch (Exception err) {
            log.warn(err.getMessage(), err);
        }
    }

    @Override
    @SneakyThrows
    public <T> T convert(Object from, Class<T> to) {
        T newInstance = to.getDeclaredConstructor().newInstance();
        try {
            BeansUtils.copyProperties(newInstance, from);
        } catch (Exception err) {
            log.warn(err.getMessage(), err);
        }
        return newInstance;
    }

    @Override
    @SneakyThrows
    public <T> T convert(Object from, Supplier<T> to) {
        T instance = to.get();
        try {
            if (instance instanceof Map) {
                Map<Object, Object> mapValue = ((Map<Object, Object>) instance);
                for (PropertyDescriptor propertyDescriptor : BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(from)) {
                    mapValue.put(propertyDescriptor.getName(), BeanUtilsBean.getInstance().getPropertyUtils().getProperty(from, propertyDescriptor.getName()));
                }
                return instance;
            }
            BeansUtils.copyProperties(instance, from);
        } catch (Exception err) {
            log.warn(err.getMessage(), err);
        }
        return instance;
    }
}
