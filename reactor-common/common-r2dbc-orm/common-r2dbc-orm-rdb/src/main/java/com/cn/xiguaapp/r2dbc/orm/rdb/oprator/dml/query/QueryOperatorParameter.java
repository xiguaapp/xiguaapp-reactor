package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;

import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.FunctionColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.FunctionTerm;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.Join;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

/**
 * @author xiguaapp
 */
@Getter
@Setter
public class QueryOperatorParameter {

    private List<SelectColumn> select = new ArrayList<>();

    private Set<String> selectExcludes = new HashSet<>();

    private String from;

    private String fromAlias;

    private List<Term> where = new ArrayList<>();

    private List<Join> joins = new ArrayList<>();

    private List<SortOrder> orderBy = new ArrayList<>();

    private List<FunctionColumn> groupBy = new ArrayList<>();

    private List<FunctionTerm> having = new ArrayList<>();

    private Integer pageIndex;

    private Integer pageSize;

    private Boolean forUpdate;

    public Optional<Join> findJoin(String targetName) {
        return Optional.ofNullable(joins)
                .flatMap(_joins -> _joins
                        .stream()
                        .filter(join -> join.equalsTargetOrAlias(targetName))
                        .findFirst());
    }

    public String getFromAlias() {
        if (fromAlias == null) {
            return from;
        }

        return fromAlias;
    }
}
