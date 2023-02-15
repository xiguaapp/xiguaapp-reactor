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


import com.cn.xiguaapp.r2dbc.orm.core.CastUtil;
import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.*;
public interface FeatureSupportedMetadata {

    Map<String, Feature> getFeatures();

    void addFeature(Feature feature);

    default List<Feature> getFeatureList() {
        return Optional.ofNullable(getFeatures())
                .map(Map::values)
                .<List<Feature>>map(ArrayList::new)
                .orElseGet(Collections::emptyList);
    }

    default <T extends Feature> List<T> getFeatures(FeatureType type) {

        return ofNullable(getFeatures())
                .map(features -> features.values()
                        .stream()
                        .filter(feature -> feature.getType().equals(type))
                        .map(CastUtil::<T>cast)
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);

    }

    default <T extends Feature> Optional<T> getFeature(FeatureId<T> id) {
        return getFeature(id.getId());
    }

    default <T extends Feature> T getFeatureNow(FeatureId<T> id) {
        return getFeatureNow(id.getId());
    }

    default <T extends Feature> Optional<T> findFeature(FeatureId<T> id) {
        return getFeature(id.getId());
    }

    default <T extends Feature> T findFeatureNow(FeatureId<T> id) {
        return this.findFeatureNow(id.getId());
    }

    default <T extends Feature> Optional<T> findFeature(String id) {
        return getFeature(id);
    }

    default <T extends Feature> T findFeatureNow(String id) {
        return this.<T>findFeature(id)
                .orElseThrow(() -> new UnsupportedOperationException("unsupported feature " + id));
    }

    default <T extends Feature> T getFeatureNow(String id) {
        return this.<T>getFeature(id)
                .orElseThrow(() -> new UnsupportedOperationException("unsupported feature " + id));
    }

    default <T extends Feature> Optional<T> getFeature(String id) {
        return ofNullable(getFeatures())
                .map(feature -> feature.get(id))
                .map(CastUtil::cast);
    }

    default <T extends Feature> Optional<T> getFeature(Feature target) {
        return ofNullable(getFeatures())
                .map(feature -> feature.get(target.getId()))
                .map(CastUtil::cast);
    }

    default boolean supportFeature(String id) {
        return getFeature(id).isPresent();
    }

    default boolean supportFeature(Feature feature) {
        return getFeature(feature.getId()).isPresent();
    }

}
