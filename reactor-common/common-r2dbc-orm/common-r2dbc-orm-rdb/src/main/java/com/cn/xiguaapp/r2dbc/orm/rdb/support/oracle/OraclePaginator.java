package com.cn.xiguaapp.r2dbc.orm.rdb.support.oracle;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.FragmentBlock;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.Paginator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.BlockSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;

/**
 * @author xiguaapp
 */
public class OraclePaginator implements Paginator {

    @Override
    public SqlFragments doPaging(SqlFragments fragments, int pageIndex, int pageSize) {
        if (fragments instanceof PrepareSqlFragments) {
            PrepareSqlFragments paging = PrepareSqlFragments.of();

            paging.addSql("select * from ( SELECT row_.*, rownum rownum_ FROM (")
                    .addFragments(fragments)
                    .addSql(") row_ ) where rownum_ <= ?  AND rownum_ > ?")
                    .addParameter((pageIndex + 1) * pageSize, pageIndex * pageSize);
            return paging;
        } else if (fragments instanceof BlockSqlFragments) {
            BlockSqlFragments block = ((BlockSqlFragments) fragments);
            block.addBlockFirst(FragmentBlock.before, PrepareSqlFragments.of().addSql("select * from ( SELECT row_.*, rownum rownum_ FROM ("));

            block.addBlock(FragmentBlock.after, PrepareSqlFragments.of().addSql(") row_ ) where rownum_ <= ?  AND rownum_ > ?")
                    .addParameter((pageIndex + 1) * pageSize, pageIndex * pageSize));
        }

        return fragments;
    }
}
