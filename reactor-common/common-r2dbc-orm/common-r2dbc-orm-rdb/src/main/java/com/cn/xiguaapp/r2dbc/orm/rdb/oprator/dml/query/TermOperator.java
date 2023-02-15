package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.param.Term;

import java.util.function.Supplier;

/**
 * @author xiguaapp
 */
public class TermOperator implements Supplier<Term> {

    private Term term = new Term();

    public TermOperator(String column, String termType, Object value) {
        term.setColumn(column);
        term.setTermType(termType);
        term.setValue(value);
    }

    @Override
    public Term get() {
        return term;
    }
}
