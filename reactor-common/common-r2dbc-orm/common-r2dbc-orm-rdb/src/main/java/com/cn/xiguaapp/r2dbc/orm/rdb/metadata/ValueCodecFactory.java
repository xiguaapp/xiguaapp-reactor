package com.cn.xiguaapp.r2dbc.orm.rdb.metadata;


import com.cn.xiguaapp.r2dbc.orm.codec.ValueCodec;
import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;

import java.util.Optional;

public interface ValueCodecFactory extends Feature {

    String ID_VALUE = "valueCodecFactory";

    FeatureId<ValueCodecFactory> ID = FeatureId.of(ID_VALUE);

    @Override
    default FeatureType getType() {
        return RDBFeatureType.codec;
    }

    @Override
    default String getName() {
        return "值编解码器";
    }

    @Override
    default String getId() {
        return ID_VALUE;
    }

    Optional<ValueCodec> createValueCodec(RDBColumnMetadata column);

}
