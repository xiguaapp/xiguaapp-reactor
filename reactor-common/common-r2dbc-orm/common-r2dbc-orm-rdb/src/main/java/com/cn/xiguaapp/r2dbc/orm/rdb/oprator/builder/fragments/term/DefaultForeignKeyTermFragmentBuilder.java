package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.term;


import com.cn.xiguaapp.r2dbc.orm.param.SqlTerm;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query.QuerySqlBuilder;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.Join;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.NativeSelectColumn;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;

import java.util.*;

public class DefaultForeignKeyTermFragmentBuilder implements ForeignKeyTermFragmentBuilder {

    public static final DefaultForeignKeyTermFragmentBuilder INSTANCE = new DefaultForeignKeyTermFragmentBuilder();

    @SuppressWarnings("all")
    public SqlFragments createFragments(String tableName, ForeignKeyMetadata key, List<Term> terms) {

        PrepareSqlFragments sqlFragments = PrepareSqlFragments.of()
                .addSql("exists(");
        key.getTarget()
                .findFeature(QuerySqlBuilder.ID)
                .ifPresent(builder -> {
                    QueryOperatorParameter parameter = new QueryOperatorParameter();
                    parameter.getSelect().add(NativeSelectColumn.of("1"));
                    parameter.setFrom(key.getTarget().getName());
                    parameter.setFromAlias(key.getAlias());
                    //关联表
                    for (ForeignKeyMetadata middleForeignKey : key.getMiddleForeignKeys()) {
                        Join join = new Join();

                        for (ForeignKeyColumn column : middleForeignKey.getColumns()) {
                            PrepareSqlFragments condition = PrepareSqlFragments.of();
                            condition.addSql(column.getSourceColumn().getFullName(key.getAlias()));
                            condition.addSql("=").addSql(column.getTargetColumn().getFullName());
                            join.getTerms().add(SqlTerm.of(condition.toRequest().getSql()));
                        }

                        if (middleForeignKey.getTerms() != null) {
                            join.getTerms().addAll(middleForeignKey.getTerms());
                        }
                        join.setAlias(middleForeignKey.getTarget().getName());
                        join.setTarget(middleForeignKey.getTarget().getFullName());
                        join.setType(middleForeignKey.getJoinType());
                        join.addAlias(middleForeignKey.getName(),
                                middleForeignKey.getAlias(),
                                middleForeignKey.getTarget().getAlias());
                        parameter.getJoins().add(join);
                    }
                    //关联条件
                    for (ForeignKeyColumn column : key.getColumns()) {
                        PrepareSqlFragments condition = PrepareSqlFragments.of();
                        if (column.getSourceColumn().getOwner().getFullName().equals(key.getSource().getFullName())) {
                            condition.addSql(column.getSourceColumn().getFullName(tableName));
                        } else {
                            condition.addSql(column.getSourceColumn().getFullName());
                        }
                        condition.addSql("=").addSql(column.getTargetColumn().getFullName(key.getAlias()));
                        parameter.getWhere().add(SqlTerm.of(condition.toRequest().getSql()));
                    }
                    Term term = new Term();
                    if (key.getTerms() != null && !key.getTerms().isEmpty()) {
                        term.setTerms(new ArrayList<>(key.getTerms()));
                        //嵌套,防止传入or导致任意数据查询问题
                        term.nest().setTerms(terms);
                    } else {
                        term.setTerms(terms);
                    }

                    parameter.getWhere().add(term);
                    SqlRequest request = builder.build(parameter);
                    if (request.isNotEmpty()) {
                        sqlFragments.addSql(request.getSql()).addParameter(request.getParameters());
                    }
                });

        return sqlFragments
                .addSql(")");
    }
}
