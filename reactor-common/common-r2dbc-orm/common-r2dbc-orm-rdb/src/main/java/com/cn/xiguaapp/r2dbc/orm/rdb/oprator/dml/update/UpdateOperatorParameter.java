package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.update;

import com.cn.xiguaapp.r2dbc.orm.param.Term;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateOperatorParameter {

    List<UpdateColumn> columns = new ArrayList<>();

    List<Term> where = new ArrayList<>();

}
