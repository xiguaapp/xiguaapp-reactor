package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;


import com.cn.xiguaapp.r2dbc.orm.core.FeatureId;
import com.cn.xiguaapp.r2dbc.orm.feature.Feature;
import com.cn.xiguaapp.r2dbc.orm.feature.FeatureType;
import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatureType;

/**
 * @author xiguaapp
 * @desc Feature异常转换接口
 * @since 1.0.0
 */
public interface ExceptionTranslation extends Feature {

    String ID_VALUE = "exceptionTranslation";

    FeatureId<ExceptionTranslation> ID = FeatureId.of(ID_VALUE);

    /**
     * 获取feature id
     * @return String
     */
    @Override
    default String getId() {
        return ID_VALUE;
    }

    /**
     * 获取RdbFeatureType描述名称
     * @see ExceptionTranslation#getType()
     * @return String
     */
    @Override
   default String getName(){
        return getType().getName();
    }

    /**
     * 获取RDBFeatureType描述
     * @see RDBFeatureType#exceptionTranslation
     * @return FeatureType
     */
    @Override
    default FeatureType getType() {
        return RDBFeatureType.exceptionTranslation;
    }

    /**
     * 异常
     * @param e
     * @return throwable
     */
    Throwable translate(Throwable e);
}
