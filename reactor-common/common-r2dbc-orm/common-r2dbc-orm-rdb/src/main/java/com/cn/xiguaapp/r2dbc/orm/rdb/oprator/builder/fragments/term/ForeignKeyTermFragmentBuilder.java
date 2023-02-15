package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.term;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.key.ForeignKeyMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;

import java.util.List;

public interface ForeignKeyTermFragmentBuilder extends Feature {

    String idValue = "foreignKeyTermFragmentBuilder";

    FeatureId<ForeignKeyTermFragmentBuilder> ID = FeatureId.of(idValue);

    @Override
    default String getId() {
        return idValue;
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    @Override
    default FeatureType getType() {
        return RDBFeatureType.foreignKeyTerm;
    }

    SqlFragments createFragments(String tableName, ForeignKeyMetadata key, List<Term> terms);


}
