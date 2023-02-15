package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.delete;

import com.cn.xiguaapp.r2dbc.orm.param.Term;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiguaapp
 */
@Getter
@Setter
public class DeleteOperatorParameter {

    private List<Term> where = new ArrayList<>();


}
