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

package com.cn.xiguaapp.r2dbc.orm.meta;

import com.cn.xiguaapp.r2dbc.orm.codec.DictionaryCodec;
import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.wrapper.PropertyWrapper;
import com.cn.xiguaapp.r2dbc.orm.wrapper.SimplePropertyWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.*;

/**
 * @author xiguaapp
 */
@Getter
@Setter
public abstract class AbstractColumnMetadata implements ColumnMetadata {
    protected String name;
    protected String alias;
    protected String comment;
    protected Class javaType;
    protected boolean updatable;
    protected boolean notNull;
    protected DictionaryCodec dictionaryCodec;
    protected ValueCodec valueCodec;
    protected DefaultValue defaultValue;
    protected Map<String, Object> properties = new HashMap<>();

    private Map<String, Feature> features = new HashMap<>();

    @Override
    public String getAlias() {
        if (alias == null) {
            alias = name;
        }
        return alias;
    }

    public Object decode(Object data) {
        if (data == null) {
            return null;
        }
        if (valueCodec != null) {
            data = valueCodec.decode(data);
        }
        if (dictionaryCodec != null) {
            data = dictionaryCodec.decode(data);
        }

        return data;
    }

    public Object encode(Object data) {
        if (data == null) {
            return null;
        }
        if (valueCodec != null) {
            data = valueCodec.encode(data);
        }
        if (dictionaryCodec != null) {
            data = dictionaryCodec.encode(data);
        }

        return data;
    }

    @Override
    public PropertyWrapper getProperty(String property) {
        return new SimplePropertyWrapper(properties.get(property));
    }

    @Override
    public PropertyWrapper getProperty(String property, Object defaultValue) {
        Object value = properties.get(property);
        return new SimplePropertyWrapper(value == null ? defaultValue : value);
    }

    @Override
    public PropertyWrapper setProperty(String property, Object value) {
        return new SimplePropertyWrapper(properties.put(property, value));
    }

    @Override
    public void addFeature(Feature feature) {
        if (features == null) {
            features = new HashMap<>();
        }
        features.put(feature.getId(), feature);
    }

    @Override
    @SneakyThrows
    public ObjectMetadata clone() {
        return (ObjectMetadata)super.clone();
    }
}
