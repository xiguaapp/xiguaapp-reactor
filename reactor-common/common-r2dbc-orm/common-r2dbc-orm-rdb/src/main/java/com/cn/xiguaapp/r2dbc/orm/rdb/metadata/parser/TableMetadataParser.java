package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.parser;

import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectType;
import com.cn.xiguaapp.r2dbc.orm.meta.RDBObjectType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author xiguaapp
 */
public interface TableMetadataParser extends ObjectMetadataParser {

    String id = "tableMetadataParser";

    @Override
    default String getId() {
        return id;
    }

    @Override
    default String getName() {
        return "表结构解析器";
    }

    @Override
    default ObjectType getObjectType() {
        return RDBObjectType.table;
    }

    List<String> parseAllTableName();

    Flux<String> parseAllTableNameReactive();

    boolean tableExists(String name);

    Mono<Boolean> tableExistsReactive(String name);
}
