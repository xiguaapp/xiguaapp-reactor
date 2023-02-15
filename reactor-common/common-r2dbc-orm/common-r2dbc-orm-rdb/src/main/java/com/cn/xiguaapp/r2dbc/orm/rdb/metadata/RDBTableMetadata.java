package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;

import com.cn.xiguaapp.r2dbc.orm.core.CastUtil;
import com.cn.xiguaapp.r2dbc.orm.meta.ObjectType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.delete.DefaultDeleteSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.BatchInsertSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.update.DefaultUpdateSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert.DefaultSaveOrUpdateOperator;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class RDBTableMetadata extends AbstractTableOrViewMetadata implements Cloneable {

    private String comment;

    private List<RDBIndexMetadata> indexes = new ArrayList<>();

    public RDBTableMetadata(String name) {
        this();
        setName(name);
    }

    public Optional<RDBIndexMetadata> getIndex(String indexName) {
        return indexes.stream()
                .filter(index -> index.getName().equalsIgnoreCase(indexName))
                .findFirst();
    }

    public RDBTableMetadata() {
        super();
        addFeature(BatchInsertSqlBuilder.of(this));
        addFeature(DefaultUpdateSqlBuilder.of(this));
        addFeature(DefaultDeleteSqlBuilder.of(this));
        addFeature(DefaultSaveOrUpdateOperator.of(this));
    }

    public void addIndex(RDBIndexMetadata index) {
        Objects.requireNonNull(index.getName(), "index name can not be null");
        index.setTableName(getName());

        indexes.add(index);

    }

    @Override
    public ObjectType getObjectType() {
        return RDBObjectType.table;
    }

    @Override
    @SneakyThrows
    public RDBTableMetadata clone() {
        RDBTableMetadata clone = (RDBTableMetadata) super.clone();
        clone.setAllColumns(new ConcurrentHashMap<>());

        this.getColumns()
                .stream()
                .map(RDBColumnMetadata::clone)
                .forEach(clone::addColumn);

        clone.setFeatures(new HashMap<>(getFeatures()));

        clone.setIndexes(getIndexes()
                .stream()
                .map(RDBIndexMetadata::clone)
                .collect(Collectors.toList()));

        this.getForeignKey()
                .stream()
                .map(ForeignKeyMetadata::clone)
                .map(CastUtil::<ForeignKeyMetadata>cast)
                .forEach(clone::addForeignKey);

        return clone;
    }

    @Override
    public void merge(TableOrViewMetadata metadata) {
        super.merge(metadata);
    }
}
