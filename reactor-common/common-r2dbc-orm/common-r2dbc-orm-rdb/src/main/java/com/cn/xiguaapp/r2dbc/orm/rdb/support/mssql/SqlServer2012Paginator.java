package com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql;


import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.FragmentBlock;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.Paginator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.BlockSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;

import java.util.LinkedList;


/**
 * @author xiguaapp
 */
public class SqlServer2012Paginator implements Paginator {
    @Override
    public SqlFragments doPaging(SqlFragments fragments, int pageIndex, int pageSize) {

        if (fragments instanceof BlockSqlFragments) {
            BlockSqlFragments block = ((BlockSqlFragments) fragments);
            LinkedList<SqlFragments> orderBy = block.getBlock(FragmentBlock.orderBy);
            if (orderBy.isEmpty()) {
                orderBy.add(SqlFragments.single("order by 1"));
            }
            block.addBlock(FragmentBlock.after, PrepareSqlFragments.of("offset ? rows fetch next ? rows only", pageIndex * pageSize, pageSize));

            return block;
        } else if (fragments instanceof PrepareSqlFragments) {
            PrepareSqlFragments sqlFragments = ((PrepareSqlFragments) fragments);
            if (!sqlFragments.getSql().contains("order by")
                    && !sqlFragments.getSql().contains("ORDER BY")) {
                sqlFragments.addSql("order", "by", "1");
            }
            sqlFragments.addSql("offset ? rows fetch next ? rows only")
                    .addParameter(pageIndex * pageSize, pageSize);
            return sqlFragments;
        }
        return fragments;
    }
}
