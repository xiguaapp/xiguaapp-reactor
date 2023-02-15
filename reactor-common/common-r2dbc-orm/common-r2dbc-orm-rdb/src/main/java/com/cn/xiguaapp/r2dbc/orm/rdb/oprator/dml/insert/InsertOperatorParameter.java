package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class InsertOperatorParameter {

    private Set<InsertColumn> columns = new LinkedHashSet<>();

    private List<List<Object>> values = new ArrayList<>();

}
