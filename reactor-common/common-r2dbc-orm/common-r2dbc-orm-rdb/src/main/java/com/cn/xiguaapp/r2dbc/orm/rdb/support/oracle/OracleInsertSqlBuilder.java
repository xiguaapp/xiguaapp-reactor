package com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle;

import com.cn.xiguaapp.r2dbc.orm.core.RuntimeDefaultValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.NullValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.EmptySqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.NativeSql;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.function.FunctionFragmentBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.insert.InsertSqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertOperatorParameter;
import lombok.AllArgsConstructor;

import java.util.*;

import static java.util.Optional.ofNullable;

@SuppressWarnings("all")
@AllArgsConstructor(staticName = "of")
public class OracleInsertSqlBuilder implements InsertSqlBuilder {
    private RDBTableMetadata table;

    @Override
    public SqlRequest build(InsertOperatorParameter parameter) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();


        Map<Integer, RDBColumnMetadata> indexMapping = new HashMap<>();
        Map<Integer, SqlFragments> functionValues = new HashMap<>();

        int index = 0;
        Set<InsertColumn> columns = parameter.getColumns();
        for (InsertColumn column : columns) {
            RDBColumnMetadata columnMetadata = ofNullable(column.getColumn())
                    .flatMap(table::getColumn)
                    .orElse(null);

            if (columnMetadata != null && columnMetadata.isInsertable()) {
                indexMapping.put(index, columnMetadata);
                //列为函数
                SqlFragments functionFragments = Optional.of(column)
                        .flatMap(insertColumn -> Optional.ofNullable(insertColumn.getFunction())
                                .flatMap(function -> columnMetadata.findFeature(FunctionFragmentBuilder.createFeatureId(function)))
                                .map(builder -> builder.create(columnMetadata.getName(), columnMetadata, insertColumn.getOpts())))
                        .orElse(EmptySqlFragments.INSTANCE);
                if (functionFragments.isNotEmpty()) {
                    functionValues.put(index, functionFragments);
                }
            }
            index++;

        }

        if (indexMapping.isEmpty()) {
            throw new IllegalArgumentException("No operable columns");
        }
        boolean batch = parameter.getValues().size() > 1;

        if (batch) {
            fragments.addSql("insert all");
        } else {
            fragments.addSql("insert");
        }

        for (List<Object> values : parameter.getValues()) {
            PrepareSqlFragments intoSql = PrepareSqlFragments.of();
            PrepareSqlFragments valuesSql = PrepareSqlFragments.of();

            intoSql.addSql("into")
                    .addSql(table.getFullName())
                    .addSql("(");

            valuesSql.addSql("values (");
            int valueLen = values.size();
            int vIndex = 0;
            for (Map.Entry<Integer, RDBColumnMetadata> entry : indexMapping.entrySet()) {
                RDBColumnMetadata column = entry.getValue();
                int valueIndex = entry.getKey();
                if (vIndex++ != 0) {
                    intoSql.addSql(",");
                    valuesSql.addSql(",");
                }
                SqlFragments function = functionValues.get(valueIndex);
                if (null != function) {
                    valuesSql.addFragments(function);
                    continue;
                }

                Object value = valueLen <= valueIndex ? null : values.get(valueIndex);

                if ((value == null || value instanceof NullValue)
                        && column.getDefaultValue() instanceof RuntimeDefaultValue) {
                    value = ((RuntimeDefaultValue) column.getDefaultValue()).get();
                }
                intoSql.addSql(column.getQuoteName());

                if (value instanceof NativeSql) {
                    valuesSql.addSql(((NativeSql) value).getSql())
                            .addParameter(((NativeSql) value).getParameters());
                    continue;
                }
                if (value == null) {
                    value = NullValue.of(column.getType());
                }
                valuesSql.addSql("?").addParameter(column.encode(value));
            }
            intoSql.addSql(")");
            valuesSql.addSql(")");

            fragments.addFragments(intoSql)
                    .addFragments(valuesSql);
        }
        if (batch) {
            fragments.addSql("select 1 from dual");
        }

        return fragments.toRequest();
    }
}
