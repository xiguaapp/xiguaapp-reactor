package com.cn.xiguaapp.r2dbc.orm.rdb.mapping.jpa;

import com.cn.xiguapp.common.core.annotations.AnnotationUtils;
import com.cn.xiguapp.common.core.utils.ClassUtils;
import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.r2dbc.orm.core.DefaultValueGenerator;
import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.DefaultEntityColumnMapping;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.EntityPropertyDescriptor;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.Comment;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.annotation.DefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser.DataTypeResolver;
import com.cn.xiguaapp.r2dbc.orm.rdb.mapping.parser.ValueCodecResolver;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.*;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.AssociationType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.utils.PropertiesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

/**
 * @author xiguaapp
 */
@Slf4j
public class JpaEntityTableMetadataParserProcessor {

    private final DefaultEntityColumnMapping mapping;

    private final Class<?> entityType;

    private final RDBTableMetadata tableMetadata;

    @Setter
    private DataTypeResolver dataTypeResolver;

    @Setter
    private ValueCodecResolver valueCodecResolver;

    public JpaEntityTableMetadataParserProcessor(RDBTableMetadata tableMetadata, Class<?> entityType) {
        this.tableMetadata = tableMetadata;
        this.entityType = entityType;
        this.mapping = new DefaultEntityColumnMapping(tableMetadata, entityType);
        tableMetadata.addFeature(this.mapping);
    }

    public void process() {
        PropertyDescriptor[] descriptors = BeanUtilsBean.getInstance()
                .getPropertyUtils()
                .getPropertyDescriptors(entityType);

        Table table = ClassUtils.getAnnotation(entityType, Table.class);
        int idx = 0;

        for (Index index : table.indexes()) {
            String name = index.name();
            if (name.isEmpty()) {
                name = tableMetadata.getName().concat("_idx_").concat(String.valueOf(idx++));
            }
            RDBIndexMetadata indexMetadata = new RDBIndexMetadata();
            indexMetadata.setUnique(index.unique());
            indexMetadata.setName(name);

            //id asc,
            String[] columnList = index.columnList().split("[,]");
            for (String str : columnList) {
                String[] columnAndSort = str.split("[ ]+");
                RDBIndexMetadata.IndexColumn column = new RDBIndexMetadata.IndexColumn();
                column.setColumn(columnAndSort[0].trim());
                if (columnAndSort.length > 1) {
                    column.setSort(columnAndSort[1].equalsIgnoreCase("desc") ? RDBIndexMetadata.IndexSort.desc : RDBIndexMetadata.IndexSort.asc);
                }
                indexMetadata.getColumns().add(column);
            }
            tableMetadata.addIndex(indexMetadata);
        }
        List<Runnable> afterRun = new ArrayList<>();

        for (PropertyDescriptor descriptor : descriptors) {
            Set<Annotation> annotations = AnnotationUtils.getAnnotations(entityType, descriptor);

            getAnnotation(annotations, Column.class)
                    .ifPresent(column -> handleColumnAnnotation(descriptor, annotations, ColumnInfo.of(column)));

            getAnnotation(annotations, JoinColumns.class)
                    .ifPresent(column -> afterRun.add(() -> handleJoinColumnAnnotation(descriptor, annotations, column.value())));

            getAnnotation(annotations, JoinColumn.class)
                    .ifPresent(column -> afterRun.add(() -> handleJoinColumnAnnotation(descriptor, annotations, column)));

        }


        afterRun.forEach(Runnable::run);
    }

    private <T extends Annotation> Optional<T> getAnnotation(Set<Annotation> annotations, Class<T> type) {
        return annotations.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }

    @SneakyThrows
    private void handleJoinColumnAnnotation(PropertyDescriptor descriptor, Set<Annotation> annotations, JoinTable column) {

    }

    @SneakyThrows
    private void handleJoinColumnAnnotation(PropertyDescriptor descriptor, Set<Annotation> annotations, JoinColumn... column) {

        Field field = PropertiesUtils.getPropertyField(entityType, descriptor.getName())
                .orElseThrow(() -> new NoSuchFieldException("no such field " + descriptor.getName() + " in " + entityType));
        Table join;
        ForeignKeyBuilder builder = ForeignKeyBuilder.builder()
                .source(tableMetadata.getFullName())
                .name(descriptor.getName())
                .alias(descriptor.getName())
                .build();

        Type fieldGenericType = field.getGenericType();
        if (fieldGenericType instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) fieldGenericType).getActualTypeArguments();
            join = Stream.of(types)
                    .map(Class.class::cast)
                    .map(t -> AnnotationUtils.getAnnotation(t, Table.class))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } else {
            builder.setAutoJoin(true);
            join = AnnotationUtils.getAnnotation(field.getType(), Table.class);
        }
        String joinTableName;
        if (join != null) {
            joinTableName = join.schema().isEmpty() ? join.name() : join.schema().concat(".").concat(join.name());
        } else {
            log.warn("can not resolve join table for :{}", field);
            return;
        }
        builder.setTarget(joinTableName);

        getAnnotation(annotations, OneToOne.class)
                .ifPresent(oneToOne -> builder.setAssociationType(AssociationType.oneToOne));
        getAnnotation(annotations, OneToMany.class)
                .ifPresent(oneToOne -> builder.setAssociationType(AssociationType.oneToMay));
        getAnnotation(annotations, ManyToMany.class)
                .ifPresent(oneToOne -> builder.setAssociationType(AssociationType.manyToMay));
        getAnnotation(annotations, ManyToOne.class)
                .ifPresent(oneToOne -> builder.setAssociationType(AssociationType.manyToOne));


        for (JoinColumn joinColumn : column) {

            String columnName = joinColumn.name();

            builder.addColumn(columnName, joinColumn.referencedColumnName());
        }
        tableMetadata.addForeignKey(builder);
    }

    @Getter
    @SuppressWarnings("all")
    private static class ColumnInfo {
        private String name = "";
        private String table = "";

        private boolean nullable;
        private boolean updatable;
        private boolean insertable;

        private int length;

        private int precision;
        private int scale;

        private String columnDefinition = "";

        public static ColumnInfo of(JoinColumn column) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.insertable = column.insertable();
            columnInfo.updatable = column.updatable();
            columnInfo.nullable = column.nullable();
            columnInfo.name = column.name();
            columnInfo.table = column.table();
            return columnInfo;
        }

        /**
         * 表字段信息
         * @param column
         * @return ColumnInfo
         */
        public static ColumnInfo of(Column column) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.insertable = column.insertable();
            columnInfo.updatable = column.updatable();
            columnInfo.nullable = column.nullable();
            columnInfo.name = column.name();
            columnInfo.table = column.table();
            columnInfo.length = column.length();
            columnInfo.scale = column.scale();
            columnInfo.precision = column.precision();

            return columnInfo;
        }
    }

    /**
     * Column注解信息annotation处理
     * @param descriptor 属性描述 {@link PropertyDescriptor}
     * @param annotations annotaion
     * @param column 字段信息
     */
    private void handleColumnAnnotation(PropertyDescriptor descriptor, Set<Annotation> annotations, ColumnInfo column) {
        //另外一个表
        if (!column.table.isEmpty() && !column.table.equals(tableMetadata.getName())) {
            mapping.addMapping(column.table.concat(".").concat(column.name), descriptor.getName());
            return;
        }
        String columnName;

        if (!column.name.isEmpty()) {
            columnName = column.name;
        } else {
            //驼峰命名
            columnName = StringUtils.camelCase2UnderScoreCase(descriptor.getName());
        }
        //获取类型
        Class<?> javaType = descriptor.getPropertyType();

        if (javaType == Object.class) {
            javaType = descriptor.getReadMethod().getReturnType();
        }
        mapping.addMapping(columnName, descriptor.getName());
        //数据库字段的基本信息属性/所有者等
        RDBColumnMetadata metadata = tableMetadata.getColumn(columnName).orElseGet(tableMetadata::newColumn);
        metadata.setName(columnName);
        metadata.setAlias(descriptor.getName());
        metadata.setJavaType(javaType);
        metadata.setLength(column.length);
        metadata.setPrecision(column.precision);
        metadata.setScale(column.scale);
        metadata.setNotNull(!column.nullable);
        metadata.setUpdatable(column.updatable);
        metadata.setInsertable(column.insertable);
        if (!column.columnDefinition.isEmpty()) {
            metadata.setColumnDefinition(column.columnDefinition);
        }
        //获取自动生成类型并作为默认值
        getAnnotation(annotations, GeneratedValue.class)
                .map(GeneratedValue::generator)
                .map(gen -> LazyDefaultValueGenerator.of(() ->
                        tableMetadata.findFeatureNow(DefaultValueGenerator.<RDBColumnMetadata>createId(gen))))
                .map(gen -> gen.generate(metadata))
                .ifPresent(metadata::setDefaultValue);
        //获取默认值
        getAnnotation(annotations, DefaultValue.class)
                .map(gen -> {
                    if (gen.value().isEmpty()) {
                        return LazyDefaultValueGenerator.of(() ->
                                tableMetadata.findFeatureNow(DefaultValueGenerator.createId(gen.generator())))
                                .generate(metadata);
                    }
                    return (RuntimeDefaultValue) gen::value;
                })
                .ifPresent(metadata::setDefaultValue);
        //获取数据库描述和字段描述并添加到metadata
        getAnnotation(annotations, Comment.class)
                .map(Comment::value)
                .ifPresent(metadata::setComment);
        //获取主键
        getAnnotation(annotations, Id.class).ifPresent(id -> metadata.setPrimaryKey(true));

        EntityPropertyDescriptor propertyDescriptor = SimpleEntityPropertyDescriptor.of(entityType, descriptor.getName(), javaType, metadata, descriptor);

        metadata.addFeature(propertyDescriptor);

        ofNullable(dataTypeResolver)
                .map(resolver -> resolver.resolve(propertyDescriptor))
                .ifPresent(metadata::setType);
        //java类型转数据库字段类型
        if (metadata.getType() == null) {
            tableMetadata
                    .getDialect()
                    .convertSqlType(metadata.getJavaType())
                    .ifPresent(jdbcType -> metadata.setJdbcType(jdbcType, metadata.getJavaType()));
        }

        ofNullable(valueCodecResolver)
                .map(resolver -> resolver.resolve(propertyDescriptor)
                        .orElseGet(() -> metadata.findFeature(ValueCodecFactory.ID)
                                .flatMap(factory -> factory.createValueCodec(metadata))
                                .orElse(null)))
                .ifPresent(metadata::setValueCodec);
        ;

        tableMetadata.addColumn(metadata);
    }


}
