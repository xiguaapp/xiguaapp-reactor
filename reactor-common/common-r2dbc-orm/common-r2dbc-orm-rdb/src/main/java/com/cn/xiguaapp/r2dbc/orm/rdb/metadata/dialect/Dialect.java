package com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.DataType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql.SqlServerDialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql.MysqlDialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleDialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres.PostgresqlDialect;

import java.sql.SQLType;
import java.util.Optional;

/**
 * 数据库方言
 *
 * @see DefaultDialect
 * @see MysqlDialect
 * @see OracleDialect
 * @see H2Dialect
 * @see PostgresqlDialect
 * @since 1.0
 */
public interface Dialect extends Feature {

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.dialect;
    }

    void addDataTypeBuilder(String typeId, DataTypeBuilder mapper);

    String buildColumnDataType(RDBColumnMetadata columnMetaData);

    String getQuoteStart();

    String getQuoteEnd();

    String clearQuote(String string);

    boolean isColumnToUpperCase();

    Optional<SQLType> convertSqlType(Class<?> type);

    DataType convertDataType(String dataType);

    default String quote(String keyword, boolean changeCase) {
        if (keyword.startsWith(getQuoteStart()) && keyword.endsWith(getQuoteEnd())) {
            return keyword;
        }
        return getQuoteStart().concat(isColumnToUpperCase() && changeCase ? keyword.toUpperCase() : keyword).concat(getQuoteEnd());
    }

    default String quote(String keyword) {
        return quote(keyword, true);
    }

    default String buildColumnFullName(String tableName, String columnName) {
        if (columnName.contains(".")) {
            return columnName;
        }
        if (StringUtils.isNullOrEmpty(tableName)) {
            return StringUtils.concat(getQuoteStart(), isColumnToUpperCase() ? columnName.toUpperCase() : columnName, getQuoteEnd());
        }
        return StringUtils.concat(tableName, ".", getQuoteStart(), isColumnToUpperCase() ? columnName.toUpperCase() : columnName, getQuoteEnd());
    }

    Dialect MYSQL = new MysqlDialect();
    Dialect ORACLE = new OracleDialect();
    Dialect H2 = new H2Dialect();
    Dialect MSSQL = new SqlServerDialect();
    Dialect POSTGRES = new PostgresqlDialect();

}
