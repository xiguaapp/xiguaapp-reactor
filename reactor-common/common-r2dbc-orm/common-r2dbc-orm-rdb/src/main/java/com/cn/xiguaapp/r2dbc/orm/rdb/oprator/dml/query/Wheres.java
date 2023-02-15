package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.param.SqlTerm;
import com.cn.xiguaapp.r2dbc.orm.param.Term;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public interface Wheres {

    static Supplier<Term> sql(String sql) {
        SqlTerm term = new SqlTerm(sql);

        return () -> term;
    }

}
