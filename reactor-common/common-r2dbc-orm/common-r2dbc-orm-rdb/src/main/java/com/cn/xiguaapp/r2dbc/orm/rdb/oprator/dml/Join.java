package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml;

import com.cn.xiguaapp.r2dbc.orm.param.SqlTerm;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

/**
 * join targetTable alias on ....
 *
 * @see SqlTerm
 */
@Getter
@Setter
public class Join {

    private JoinType type;

    private String target;

    private String alias;

    private Set<String> aliasList = new HashSet<>();

    private List<Term> terms = new ArrayList<>();

    public void addAlias(String... alias) {
        aliasList.addAll(Arrays.asList(alias));

    }

    public String getAlias() {
        if (alias == null) {
            return target;
        }
        return alias;
    }

    public boolean equalsTargetOrAlias(String name) {
        return getAlias().equalsIgnoreCase(name)
                ||
                getTarget().equalsIgnoreCase(name)
                || aliasList.contains(name);
    }
}
