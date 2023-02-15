package com.cn.xiguaapp.r2dbc.orm.rdb.support.mysql;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.FragmentBlock;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.Paginator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.BlockSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;

/**
 * @author xiguaapp
 */
public class MysqlPaginator implements Paginator {
    @Override
    public SqlFragments doPaging(SqlFragments fragments, int pageIndex, int pageSize) {

        if (fragments instanceof PrepareSqlFragments) {
            ((PrepareSqlFragments) fragments)
                    .addSql("limit ?,?")
                    .addParameter(pageIndex * pageSize, pageSize);

        } else if (fragments instanceof BlockSqlFragments) {
            ((BlockSqlFragments) fragments).addBlock(FragmentBlock.after, PrepareSqlFragments.of()
                    .addSql("limit ?,?")
                    .addParameter(pageIndex * pageSize, pageSize));
        }


        return fragments;
    }
}
