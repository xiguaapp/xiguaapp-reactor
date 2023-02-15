package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.query;

import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query.QueryOperatorParameter;

public interface QuerySqlFragmentBuilder extends Feature {

    //feature id
    String where = "queryTermsFragmentBuilder";
    String selectColumns = "selectColumnFragmentBuilder";
    String join = "joinSqlFragmentBuilder";
    String sortOrder = "sortOrderFragmentBuilder";


    @Override
    default FeatureType getType() {
        return RDBFeatureType.fragment;
    }

    SqlFragments createFragments(QueryOperatorParameter parameter);

}
