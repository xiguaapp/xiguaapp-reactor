package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.upsert;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;

/**
 * @author xiguaapp
 */
public interface SaveOrUpdateOperator extends Feature {

    String ID_VALUE = "saveOrUpdateOperator";

    FeatureId<SaveOrUpdateOperator> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    @Override
    default FeatureType getType() {
        return RDBFeatureType.saveOrUpdateOperator;
    }

    SaveResultOperator execute(UpsertOperatorParameter parameter);

}
