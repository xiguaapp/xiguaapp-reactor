package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatures;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.TableOrViewMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.PrepareSqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.Join;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.JoinType;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;
import lombok.AllArgsConstructor;

import java.util.List;

import static java.util.Optional.*;

@AllArgsConstructor(staticName = "of")
public class JoinFragmentBuilder implements QuerySqlFragmentBuilder {

    private TableOrViewMetadata metadata;

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {

        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        List<Join> joins = parameter.getJoins();

        for (Join join : joins) {
            metadata.getSchema()
                    .findTableOrView(join.getTarget())
                    .ifPresent(target -> {
                        ofNullable(join.getType())
                                .map(JoinType::name)
                                .ifPresent(fragments::addSql);
                        //join schema.table on
                        fragments.addSql("join")
                                .addSql(target.getFullName())
                                .addSql(join.getAlias())
                                .addSql("on");

                        fragments.addFragments(
                                target.getFeature(RDBFeatures.where)
                                        .map(builder -> {

                                            QueryOperatorParameter joinOnParameter = new QueryOperatorParameter();
                                            joinOnParameter.setFrom(target.getName());
                                            joinOnParameter.setFromAlias(join.getAlias());
                                            if(join.getTerms()!=null) {
                                                joinOnParameter.getWhere().addAll(join.getTerms());
                                            }
                                            return builder.createFragments(joinOnParameter);
                                        })
                                        .filter(SqlFragments::isNotEmpty)
                                        .orElseThrow(() -> new IllegalArgumentException("join terms is empty"))
                        );
                    });

        }

        return fragments;
    }

    @Override
    public String getId() {
        return join;
    }

    @Override
    public String getName() {
        return "表连接";
    }
}
