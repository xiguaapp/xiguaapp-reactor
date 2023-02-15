package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.function;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.EmptySqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class SimpleFunctionFragmentBuilder implements FunctionFragmentBuilder {

    private final String function;

    private final String name;


    @Override
    public SqlFragments create(String columnFullName, RDBColumnMetadata metadata, Map<String, Object> opts) {

        if (opts != null) {
            String arg = String.valueOf(opts.get("arg"));
            if ("1".equals(arg)) {
                columnFullName = arg;
            }
        }
        if (columnFullName == null) {
            return EmptySqlFragments.INSTANCE;
        }
        return PrepareSqlFragments
                .of()
                .addSql(function.concat("(").concat(columnFullName).concat(")"));
    }


}
