package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.term;


import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;

/**
 * @author xiguaapp
 */
public class EmptyTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private String symbol;

    public EmptyTermFragmentBuilder(String termType, String name, boolean isNot) {
        super(termType, name);
        symbol = isNot ? "=" : "!=";
    }

    @Override
    public PrepareSqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term) {

        // column = ?
        return PrepareSqlFragments.of()
                .addSql(columnFullName, symbol, "''");
    }
}
