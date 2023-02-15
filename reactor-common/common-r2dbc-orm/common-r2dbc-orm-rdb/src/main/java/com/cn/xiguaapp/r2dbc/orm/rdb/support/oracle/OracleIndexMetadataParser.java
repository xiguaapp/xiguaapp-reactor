package com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle;

import com.cn.xiguaapp.r2dbc.orm.core.CastUtil;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequests;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SyncSqlExecutor;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ColumnWrapperContext;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrapper;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBIndexMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.parser.IndexMetadataParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.cn.xiguaapp.r2dbc.orm.rdb.executor.wrapper.ResultWrappers.*;

/**
 * @author xiguaapp
 */
@Slf4j
@AllArgsConstructor(staticName = "of")
public class OracleIndexMetadataParser implements IndexMetadataParser {

    private static final String sql = "select idx.index_name," +
            "idx.table_name," +
            "idx.uniqueness," +
            "col.column_name," +
            "col.column_position," +
            "col.descend from all_ind_columns col " +
            "join all_indexes idx on col.index_name = idx.index_name " +
            "where idx.table_owner=? and upper(idx.table_name)= ?";

    private static final String virtualColumnSql = "select column_name,data_default from all_tab_cols" +
            " where owner=? and upper(table_name) = ? and virtual_column='YES'";

    private static final String primaryKeyIndexSql = "select index_name from all_constraints where " +
            "owner=? and upper(table_name) = ? and constraint_type = 'P'";

    private final RDBSchemaMetadata schema;

    @Override
    public List<RDBIndexMetadata> parseTableIndex(String tableName) {
        String schemaName = schema.getName().toUpperCase();
        String tableUpperName = tableName.toUpperCase();

        return schema.<SyncSqlExecutor>findFeature(SyncSqlExecutor.ID)
                .map(sqlExecutor -> sqlExecutor.select(SqlRequests.prepare(sql, schemaName, tableUpperName),
                        new OracleIndexWrapper(
                                sqlExecutor
                                        .select(SqlRequests.prepare(virtualColumnSql, schemaName, tableUpperName), ResultWrappers.lowerCase(mapStream()))
                                        .map(CastUtil::<Map<String, String>>cast)
                                        .collect(Collectors.toMap(map -> map.get("column_name"), map -> map.get("data_default")))
                                ,
                                sqlExecutor
                                        .select(SqlRequests.prepare(primaryKeyIndexSql, schemaName, tableUpperName),
                                                stream(column("index_name", String::valueOf)))
                                        .collect(Collectors.toSet())
                        )))
                .orElseGet(() -> {
                    log.warn("unsupported SyncSqlExecutor");
                    return Collections.emptyList();
                });
    }

    @Override
    public Optional<RDBIndexMetadata> parseByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<RDBIndexMetadata> parseAll() {
        return Collections.emptyList();
    }

    @Override
    public Flux<RDBIndexMetadata> parseAllReactive() {
        // TODO: 2020/8/17
        return Flux.empty();
    }

    @Override
    public Mono<RDBIndexMetadata> parseByNameReactive(String name) {
        return Mono.empty();
    }

    @Override
    public Flux<RDBIndexMetadata> parseTableIndexReactive(String tableName) {
        return Flux.empty();
    }

    class OracleIndexWrapper implements ResultWrapper<Map<String, String>, List<RDBIndexMetadata>> {
        private Map<String, RDBIndexMetadata> mappingByName = new HashMap<>();
        private Map<String, String> virtualColumn;
        private Set<String> indexName;

        public OracleIndexWrapper(Map<String, String> virtualColumn, Set<String> indexName) {
            this.virtualColumn = virtualColumn;
            this.indexName = indexName;
        }

        @Override
        public Map<String, String> newRowInstance() {
            return new HashMap<>();
        }

        @Override
        public void wrapColumn(ColumnWrapperContext<Map<String, String>> context) {
            if (context.getResult() == null) {
                return;
            }
            context.getRowInstance().put(context.getColumnLabel().toLowerCase(), String.valueOf(context.getResult()));
        }

        @Override
        public boolean completedWrapRow(Map<String, String> result) {
            RDBIndexMetadata metadata = mappingByName.computeIfAbsent(result.get("index_name"), RDBIndexMetadata::new);
            metadata.setTableName(result.get("table_name"));
            metadata.setUnique("UNIQUE".equals(result.get("uniqueness")));
            metadata.setPrimaryKey(indexName.contains(metadata.getName()));
            RDBIndexMetadata.IndexColumn column = new RDBIndexMetadata.IndexColumn();
            column.setSort("ASC".equalsIgnoreCase(result.get("descend")) ?
                    RDBIndexMetadata.IndexSort.asc :
                    RDBIndexMetadata.IndexSort.desc);
            column.setSortIndex(Integer.parseInt(result.get("column_position")));
            String columnName = result.get("column_name");

            column.setColumn(schema.getDialect().clearQuote(virtualColumn.getOrDefault(columnName, columnName)).toLowerCase());

            metadata.getColumns().add(column);
            return true;
        }

        @Override
        public List<RDBIndexMetadata> getResult() {
            return new ArrayList<>(mappingByName.values());
        }
    }
}
