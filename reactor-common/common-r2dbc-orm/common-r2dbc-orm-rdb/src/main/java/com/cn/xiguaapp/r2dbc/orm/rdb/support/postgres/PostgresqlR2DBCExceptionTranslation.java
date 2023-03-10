package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;

import com.cn.xiguaapp.r2dbc.orm.rdb.exception.DuplicateKeyException;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ExceptionTranslation;
import io.r2dbc.postgresql.api.ErrorDetails;
import io.r2dbc.postgresql.api.PostgresqlException;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 */
@AllArgsConstructor(staticName = "of")
public class PostgresqlR2DBCExceptionTranslation implements ExceptionTranslation {

    private RDBSchemaMetadata schema;

    @Override
    public Throwable translate(Throwable e) {
        if (e instanceof PostgresqlException) {
            ErrorDetails details = ((PostgresqlException) e).getErrorDetails();
            if (details.getMessage().startsWith("duplicate key") && details.getTableName().isPresent() && details.getConstraintName().isPresent()) {
                String tableName = details.getTableName().get();
                String constraintName = details.getConstraintName().get();
                return schema.getTable(tableName)
                        // TODO: 2019-11-04 更多异常判断
                        .flatMap(table -> table.getIndex(constraintName)
                                .<Throwable>map(idx -> new DuplicateKeyException(idx.isPrimaryKey(),
                                        idx.getColumns()
                                                .stream()
                                                .map(indexColumn ->
                                                        table.getColumn(indexColumn.getColumn())
                                                                .orElse(null))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList()), e)))
                        .orElseGet(() -> new DuplicateKeyException(false, Collections.emptyList(), e));

            }
        }
        return e;
    }
}
