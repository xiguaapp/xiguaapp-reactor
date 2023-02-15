/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:55 >
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

import com.cn.ykyoung.server.common.param.ValidationAnnotationDefinition;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author xiguaapp
 */
public abstract class AbstractValidationAnnotationBuilder<T extends Annotation> implements ValidationAnnotationBuilder<T> {
    @Override
    public ValidationAnnotationDefinition build(T jsr303Annotation) {
        ValidationAnnotationDefinition validationAnnotationDefinition = new ValidationAnnotationDefinition();

        validationAnnotationDefinition.setAnnotationClass(jsr303Annotation.annotationType());
        Map<String, Object> properties = AnnotationUtils.getAnnotationAttributes(jsr303Annotation);
        properties = this.formatProperties(properties);
        validationAnnotationDefinition.setProperties(properties);
        return validationAnnotationDefinition;
    }

    protected Map<String, Object> formatProperties(Map<String, Object> properties) {
        Set<Map.Entry<String, Object>> entrySet = properties.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            Object value = entry.getValue();
            if (value.getClass().isArray()) {
                Object[] arr = (Object[])value;
                if (arr.length == 0) {
                    properties.put(entry.getKey(), null);
                }
            }
        }
        return properties;
    }
}
