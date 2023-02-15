package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments;

import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;

/**
 * @author xiguaapp
 * @since 1.0.0
 * @desc SQL条件片段构造器
 */
public interface TermFragmentBuilder extends Feature {

    static FeatureId<TermFragmentBuilder> createFeatureId(String suffix){
        return FeatureId.of(RDBFeatureType.termType.getId().concat(":").concat(suffix.toLowerCase()));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getTermType().toLowerCase());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.termType;
    }

    String getTermType();

    SqlFragments createFragments(String columnFullName, RDBColumnMetadata column, Term term);

}
