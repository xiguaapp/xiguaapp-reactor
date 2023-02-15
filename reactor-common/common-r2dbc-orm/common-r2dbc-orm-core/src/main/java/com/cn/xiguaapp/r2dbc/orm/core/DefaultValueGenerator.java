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

package com.cn.xiguaapp.r2dbc.orm.core;


import com.cn.xiguaapp.r2dbc.orm.feature.DefaultFeatureType;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadata;

public interface DefaultValueGenerator<E extends ObjectMetadata> extends Feature {

    static <E extends ObjectMetadata> FeatureId<DefaultValueGenerator<E>> createId(String id) {
        return FeatureId.of("generator_".concat(id));
    }

    String getSortId();

    @Override
    default String getId() {
        return "generator_".concat(getSortId());
    }

    @Override
    default FeatureType getType() {
        return DefaultFeatureType.defaultValueGenerator;
    }

    DefaultValue generate(E metadata);

}
