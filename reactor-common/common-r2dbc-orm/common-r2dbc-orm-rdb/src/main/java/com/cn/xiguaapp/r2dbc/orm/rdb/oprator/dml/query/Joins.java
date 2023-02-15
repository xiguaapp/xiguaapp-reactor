package com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.query;


import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.dml.JoinType;

/**
 * @author xiguaapp
 */
public interface Joins {


    static JoinOperator left(String target) {
        return new JoinOperator(target, JoinType.left);
    }

    static JoinOperator inner(String target) {
        return new JoinOperator(target, JoinType.inner);
    }

    static JoinOperator right(String target) {
        return new JoinOperator(target, JoinType.right);
    }

    static JoinOperator full(String target) {
        return new JoinOperator(target, JoinType.full);
    }
}
