package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert;

import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.insert.InsertColumn;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpsertOperatorParameter {

    private Set<UpsertColumn> columns = new LinkedHashSet<>();

    private List<List<Object>> values = new ArrayList<>();

    private boolean doNothingOnConflict = false;

    @SuppressWarnings("all")
    public Set<InsertColumn> toInsertColumns() {
        return (Set) columns;
    }
}
