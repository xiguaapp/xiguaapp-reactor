package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.parser;

import com.cn.xiguaapp.r2dbc.orm.meta.ObjectMetadataParser;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectType;
import com.cn.xiguaapp.r2dbc.orm.meta.RDBObjectType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author xiguaapp
 */
public interface IndexMetadataParser extends ObjectMetadataParser {

    String id = "indexMetadataParser";

    @Override
    default String getId() {
        return id;
    }

    @Override
    default String getName() {
        return "索引解析器";
    }

    @Override
    default ObjectType getObjectType() {
        return RDBObjectType.index;
    }

    List<RDBIndexMetadata> parseTableIndex(String tableName);

    @Override
    List<RDBIndexMetadata> parseAll();

    Flux<RDBIndexMetadata> parseTableIndexReactive(String tableName);

    @Override
    Flux<RDBIndexMetadata> parseAllReactive();
}
