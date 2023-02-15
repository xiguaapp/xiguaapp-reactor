package com.cn.xiguaapp.r2dbc.orm.rdb.support.mssql;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.FragmentBlock;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.Paginator;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.BlockSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.FragmentBlock.selectColumn;


/**
 * select top pageSize *
 * from (select row_number()
 * over(order by sno asc) as rownumber,*
 * from student) temp_row
 * where rownumber>((pageIndex-1)*pageSize);
 * @author xiguaapp
 */
@Slf4j
public class SqlServerPaginator implements Paginator {
    @Override
    public SqlFragments doPaging(SqlFragments fragments, int pageIndex, int pageSize) {

        if (fragments instanceof BlockSqlFragments) {
            BlockSqlFragments newBlock = BlockSqlFragments.of();

            BlockSqlFragments block = ((BlockSqlFragments) fragments);

            newBlock.addBlock(FragmentBlock.before, "select top " + pageSize + " * from (");

            block.getBlock(selectColumn)
                    .add(SqlFragments.single(", row_number() over(order by (SELECT 1)) as rownumber"));

            List<SqlFragments> newOrderBy = block.getBlock(FragmentBlock.orderBy)
                    .stream()
                    .map(frg -> {
                        // TODO: 2019-09-20 不太严谨的做法，将排序指定的表名替换为_row
                        return PrepareSqlFragments.of(frg.getSql()
                                        .stream()
                                        .map(sql -> {
                                            if (sql.contains(".")) {
                                                String[] arr = sql.split("[.]");
                                                arr[0] = "_row";
                                                return String.join(".", arr);
                                            }
                                            return sql;
                                        }).collect(Collectors.toList()),
                                frg.getParameters());
                    }).collect(Collectors.toList());
            block.getBlock(FragmentBlock.orderBy).clear();
            newBlock.addBlock(FragmentBlock.other, block);

            newBlock.addBlock(FragmentBlock.after, PrepareSqlFragments.of().addSql(") _row where _row.rownumber > ?")
                    .addParameter(pageIndex * pageSize));

            newBlock.getBlock(FragmentBlock.after).addAll(newOrderBy);

            return newBlock;

        } else {
            log.warn("unsupported sql fragments type [{}] paging ", fragments.getClass());
        }


        return fragments;
    }
}
