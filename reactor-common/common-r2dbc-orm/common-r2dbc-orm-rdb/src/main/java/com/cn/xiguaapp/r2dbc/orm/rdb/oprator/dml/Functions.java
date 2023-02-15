package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBFeatures;

/**
 * @author xiguaapp
 */
public interface Functions {


    static FunctionColumnOperator count(String column){
        return new FunctionColumnOperator(RDBFeatures.count.getFunction(),column);
    }

    static FunctionColumnOperator sum(String column){
        return new FunctionColumnOperator(RDBFeatures.sum.getFunction(),column);
    }

}
