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
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

@Slf4j
public abstract class AbstractSchemaMetadata implements SchemaMetadata {

    private final Map<String, Map<String, ObjectMetadata>> metaRepository = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private DatabaseMetadata<?> database;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String alias;

    @Getter
    private Map<String, Feature> features = new HashMap<>();

    @Override
    public abstract List<ObjectType> getAllObjectType();

    @Override
    public ObjectType getObjectType() {
        return DefaultObjectType.schema;
    }

    @Override
    @SuppressWarnings("all")
    public <T extends ObjectMetadata> List<T> getObject(ObjectType type) {
        Map<String, ObjectMetadata> typeMapping = metaRepository.get(type.getId());
        if (typeMapping == null) {

            List<T> all = loadMetadata(type);
            Map<String, ObjectMetadata> group = all.stream()
                                                   .collect(toMap(ObjectMetadata::getName, Function.identity(), (_1, _2) -> _1, ConcurrentHashMap::new));

            typeMapping = metaRepository.put(type.getId(), group);
            if (typeMapping != null) {
                typeMapping.forEach(group::putIfAbsent);
            }
            return all;
        }
        return (List) new ArrayList<>(typeMapping.values());
    }

    public <T extends ObjectMetadata> Flux<T> getObjectReactive(ObjectType type) {
        Map<String, ObjectMetadata> typeMapping = metaRepository.get(type.getId());
        if (typeMapping == null) {
            return loadMetadataReactive(type)
                    .collectMap(ObjectMetadata::getName, Function.identity())
                    .flatMapMany(group -> {
                        Map<String, ObjectMetadata> mapping = metaRepository.put(type.getId(), group);
                        if (mapping != null) {
                            mapping.forEach(group::putIfAbsent);
                        }
                        return Flux.fromIterable(group.values());
                    })
                    .map(CastUtil::cast);
        }
        return Flux.fromIterable(typeMapping.values()).map(CastUtil::cast);
    }

    protected <T extends ObjectMetadata> List<T> loadMetadata(ObjectType type) {
        return getParser(type)
                .map(ObjectMetadataParser::parseAll)
                .map(CastUtil::<List<T>>cast)
                .orElseGet(Collections::emptyList);
    }

    protected <T extends ObjectMetadata> T loadMetadata(ObjectType type, String name) {
        return getParser(type)
                .flatMap(parser -> {
                    log.debug("load {} metadata ,use parser:{}", type, parser.getClass().getSimpleName());
                    return parser.parseByName(name);
                })
                .map(CastUtil::<T>cast)
                .orElse(null);
    }

    protected <T extends ObjectMetadata> Flux<T> loadMetadataReactive(ObjectType type) {
        return Mono.justOrEmpty(getParser(type))
                   .flatMapMany(ObjectMetadataParser::parseAllReactive)
                   .map(CastUtil::cast);
    }

    protected <T extends ObjectMetadata> Mono<T> loadMetadataReactive(ObjectType type, String name) {
        return Mono.justOrEmpty(getParser(type))
                   .flatMap(parser -> {
                       log.debug("reactive load {} [{}] metadata ,use parser:{}", type, name, parser
                               .getClass()
                               .getSimpleName());
                       return parser.parseByNameReactive(name);
                   })
                   .map(CastUtil::cast);
    }

    protected Optional<ObjectMetadataParser> getParser(ObjectType type) {
        return getFeatures().values()
                            .stream()
                            .filter(ObjectMetadataParser.class::isInstance)
                            .map(ObjectMetadataParser.class::cast)
                            .filter(parser -> parser.getObjectType().getId().equals(type.getId()))
                            .findFirst();
    }

    @Override
    public void addObject(ObjectMetadata metadata) {
        Map<String, ObjectMetadata> repo = metaRepository.computeIfAbsent(metadata
                                                                                  .getObjectType()
                                                                                  .getId(), t -> new ConcurrentHashMap<>());

        repo.put(metadata.getName(), metadata);
        if (metadata.getAlias() != null) {
            repo.put(metadata.getAlias(), metadata);
        }
    }

    @Override
    public <T extends ObjectMetadata> Optional<T> removeObject(ObjectType type, String name) {
        Objects.requireNonNull(name, "name");

        return ofNullable(metaRepository.get(type.getId()))
                .map(repo -> repo.remove(name))
                .map(CastUtil::cast);
    }

    public <T extends ObjectMetadata> Mono<T> getObjectReactive(ObjectType type, String name, boolean autoLoad) {
        Objects.requireNonNull(name, "name");
        Map<String, ObjectMetadata> mapping = metaRepository.computeIfAbsent(type.getId(), t -> new ConcurrentHashMap<>());

        if (mapping.get(name) == null && autoLoad) {
            return loadMetadataReactive(type, name)
                    .doOnNext(obj -> mapping.put(name, obj))
                    .map(CastUtil::cast);
        }
        return Mono
                .justOrEmpty(mapping.get(name))
                .map(CastUtil::cast)
                ;
    }

    public <T extends ObjectMetadata> Mono<T> getObjectReactive(ObjectType type, String name) {
        return getObjectReactive(type, name, true);
    }

    @Override
    public <T extends ObjectMetadata> Optional<T> getObject(ObjectType type, String name) {
        return getObject(type, name, false);
    }

    @Override
    public <T extends ObjectMetadata> Optional<T> getObject(ObjectType type, String name, boolean autoLoad) {
        Objects.requireNonNull(name, "name");
        return of(metaRepository.computeIfAbsent(type.getId(), t -> new ConcurrentHashMap<>()))
                .map(repo -> repo.computeIfAbsent(name, __ -> autoLoad ? loadMetadata(type, name) : null))
                .map(CastUtil::cast);
    }

    @Override
    public void addFeature(Feature feature) {
        features.put(feature.getId(), feature);
    }

    @Override
    public <T extends Feature> Optional<T> findFeature(FeatureId<T> id) {
        return findFeature(id.getId());
    }

    @Override
    public <T extends Feature> Optional<T> findFeature(String id) {
        return of(this.<T>getFeature(id))
                .filter(Optional::isPresent)
                .orElseGet(() -> Optional
                        .ofNullable(getDatabase())
                        .flatMap(database -> database.getFeature(id)));
    }

    @Override
    @SneakyThrows
    public AbstractSchemaMetadata clone() {
        AbstractSchemaMetadata schema = (AbstractSchemaMetadata) super.clone();
        schema.features = new HashMap<>(features);
        return schema;
    }
}
