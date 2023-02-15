package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml;

import com.cn.xiguaapp.r2dbc.orm.param.Term;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author xiguaapp
 */
@Getter
@Setter
public class FunctionTerm extends Term {

    private String function;

    private Map<String, String> opts;

}
