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
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import static java.util.Optional.*;

public abstract class AbstractDatabaseMetadata<S extends SchemaMetadata>
        implements DatabaseMetadata<S>, FeatureSupportedMetadata {

    private Map<String, S> schemas = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private S currentSchema;

    @Getter
    @Setter
    protected String databaseName;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String alias;

    @Getter
    private Map<String, Feature> features = new HashMap<>();

    public void addSchema(S schema) {
        schemas.put(schema.getName(), schema);
        if (schema.getAlias() != null) {
            schemas.put(schema.getAlias(), schema);
        }
    }

    @Override
    public List<S> getSchemas() {
        return new ArrayList<>(schemas.values());
    }

    @Override
    public Optional<S> getSchema(String name) {
        return ofNullable(schemas.get(name));
    }

    @Override
    public <T extends ObjectMetadata> Optional<T> getObject(String name, BiFunction<S, String, Optional<T>> mapper) {
        if (name == null) {
            return empty();
        }
        if (name.contains(".")) {
            String[] arr = name.split("[.]");
            return this.getSchema(arr[0])
                    .flatMap(schema -> mapper.apply(schema, arr[1]));
        }
        return of(this.getCurrentSchema())
                .flatMap(schema -> mapper.apply(schema, name));
    }

    @Override
    public <T extends ObjectMetadata> Mono<T> getObjectReactive(String name, BiFunction<S, String, Mono<T>> mapper) {
        if (name == null) {
            return Mono.empty();
        }
        if (name.contains(".")) {
            String[] arr = name.split("[.]");
            return Mono.justOrEmpty(this.getSchema(arr[0]))
                    .flatMap(schema -> mapper.apply(schema, arr[1]));
        }
        return Mono.just(this.getCurrentSchema())
                .flatMap(schema -> mapper.apply(schema, name));
    }

    @Override
    public void addFeature(Feature feature) {
        features.put(feature.getId(), feature);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public AbstractDatabaseMetadata<S> clone() {
        AbstractDatabaseMetadata<S> metadata = (AbstractDatabaseMetadata) super.clone();
        metadata.schemas = new ConcurrentHashMap<>();
        getSchemas().stream()
                .map(SchemaMetadata::clone)
                .map(CastUtil::<S>cast)
                .forEach(metadata::addSchema);

        metadata.features = new HashMap<>(getFeatures());

        return metadata;
    }
}
