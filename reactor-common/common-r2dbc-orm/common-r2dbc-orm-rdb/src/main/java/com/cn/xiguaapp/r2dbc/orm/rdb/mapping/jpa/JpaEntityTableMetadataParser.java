package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa;


import com.cn.xiguapp.common.core.annotations.AnnotationUtils;
import com.cn.xiguapp.common.core.utils.ClassUtils;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser.*;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import lombok.Setter;

import javax.persistence.Table;
import java.util.Optional;

/**
 * @author xiguaapp
 * @desc 实体类转化表结构
 * @see javax.persistence.Column
 * @see javax.persistence.JoinTable
 * @see javax.persistence.JoinColumn
 * @since 1.0
 */
public class JpaEntityTableMetadataParser implements EntityTableMetadataParser {
    /**
     * 数据库封装信息
     */
    @Setter
    private RDBDatabaseMetadata databaseMetadata;
    /**
     * 字段类型描述信息
     */
    @Setter
    private DataTypeResolver dataTypeResolver = DefaultDataTypeResolver.INSTANCE;
    /**
     *
     */
    @Setter
    private ValueCodecResolver valueCodecResolver = DefaultValueCodecResolver.COMMONS;


    @Override
    public Optional<RDBTableMetadata> parseTableMetadata(Class<?> entityType) {

        Table table = AnnotationUtils.getAnnotation(entityType, Table.class);
        if (table == null) {
            return Optional.empty();
        }
        RDBSchemaMetadata schema = databaseMetadata.getSchema(table.schema())
                .orElseGet(databaseMetadata::getCurrentSchema);


        RDBTableMetadata tableMetadata = schema.newTable(table.name());
        //tableMetadata.setAlias(entityType.getSimpleName());

        Optional.ofNullable(ClassUtils.getAnnotation(entityType, Comment.class))
                .map(Comment::value)
                .ifPresent(tableMetadata::setComment);

        JpaEntityTableMetadataParserProcessor parserProcessor = new JpaEntityTableMetadataParserProcessor(tableMetadata, entityType);
        parserProcessor.setDataTypeResolver(dataTypeResolver);
        parserProcessor.setValueCodecResolver(valueCodecResolver);
        parserProcessor.process();


        return Optional.of(tableMetadata);

    }


}
