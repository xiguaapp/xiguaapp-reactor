package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser;



import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;

import java.util.Optional;

public interface EntityTableMetadataParser {

    Optional<RDBTableMetadata> parseTableMetadata(Class<?> entityType);

}
