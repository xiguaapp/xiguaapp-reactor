package com.cn.xiguaapp.r2dbc.common.core.configuration;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.dialect.Dialect;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.h2.H2SchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql.SqlServerSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql.MysqlSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle.OracleSchemaMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres.PostgresqlSchemaMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xiguaapp
 * @desc orm自动配置类
 * @since 1.0 18:06
 */
@ConfigurationProperties(prefix = EasyormProperties.PREFIX)
@Data
public class EasyormProperties {
    public static final String PREFIX =  "easyorm";
    private String defaultSchema="PUBLIC";

    private String[] schemas = {};

    private boolean autoDdl = true;

    private boolean allowAlter = false;

    private boolean allowTypeAlter = true;

    private DialectEnum dialect = DialectEnum.h2;

    private Class<? extends Dialect> dialectType;

    private Class<? extends RDBSchemaMetadata> schemaType;

    public RDBDatabaseMetadata createDatabaseMetadata() {
        RDBDatabaseMetadata metadata = new RDBDatabaseMetadata(createDialect());

        Set<String> schemaSet = new HashSet<>(Arrays.asList(schemas));
        if (defaultSchema != null) {
            schemaSet.add(defaultSchema);
        }
        schemaSet.stream()
                .map(this::createSchema)
                .forEach(metadata::addSchema);

        metadata.getSchema(defaultSchema)
                .ifPresent(metadata::setCurrentSchema);

        return metadata;
    }

    @SneakyThrows
    public RDBSchemaMetadata createSchema(String name) {
        if (schemaType == null) {
            return dialect.createSchema(name);
        }
        return schemaType.getConstructor(String.class).newInstance(name);
    }

    @SneakyThrows
    public Dialect createDialect() {
        if (dialectType == null) {
            return dialect.getDialect();
        }

        return dialectType.getDeclaredConstructor().newInstance();
    }

    @Getter
    @AllArgsConstructor
    public enum DialectEnum {
        mysql(Dialect.MYSQL, "?") {
            @Override
            public RDBSchemaMetadata createSchema(String name) {
                return new MysqlSchemaMetadata(name);
            }
        },
        mssql(Dialect.MSSQL, "@arg") {
            @Override
            public RDBSchemaMetadata createSchema(String name) {
                return new SqlServerSchemaMetadata(name);
            }
        },
        oracle(Dialect.ORACLE, "?") {
            @Override
            public RDBSchemaMetadata createSchema(String name) {
                return new OracleSchemaMetadata(name);
            }
        },
        postgres(Dialect.POSTGRES, "$") {
            @Override
            public RDBSchemaMetadata createSchema(String name) {
                return new PostgresqlSchemaMetadata(name);
            }
        },
        h2(Dialect.H2, "$") {
            @Override
            public RDBSchemaMetadata createSchema(String name) {
                return new H2SchemaMetadata(name);
            }
        },
        ;

        private Dialect dialect;
        private String bindSymbol;

        public abstract RDBSchemaMetadata createSchema(String name);
    }
}
