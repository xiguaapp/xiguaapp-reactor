package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.function;

import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.builder.fragments.SqlFragments;

import java.util.Map;

public interface FunctionFragmentBuilder extends Feature {

    static FeatureId<FunctionFragmentBuilder> createFeatureId(String suffix){
        return FeatureId.of(RDBFeatureType.function.getId().concat(":").concat(suffix));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getFunction());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.function;
    }

    String getFunction();

    SqlFragments create(String columnFullName, RDBColumnMetadata metadata, Map<String, Object> opts);

}
