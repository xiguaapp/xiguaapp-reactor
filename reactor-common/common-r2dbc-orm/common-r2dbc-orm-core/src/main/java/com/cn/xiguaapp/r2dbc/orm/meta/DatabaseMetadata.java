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

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;


/**
 * @author xiguaapp
 * @desc 数据库
 * @since 4.0.0
 */
public interface DatabaseMetadata<S extends SchemaMetadata> extends ObjectMetadata,FeatureSupportedMetadata {

    @Override
    String getName();

    S getCurrentSchema();

    List<S> getSchemas();

    Optional<S> getSchema(String name);

    <T extends ObjectMetadata> Optional<T> getObject(String name, BiFunction<S, String,  Optional<T>> mapper);

    <T extends ObjectMetadata> Mono<T> getObjectReactive(String name, BiFunction<S, String,  Mono<T>> mapper);

    @Override
    default ObjectType getObjectType() {
        return DefaultObjectType.database;
    }
}
