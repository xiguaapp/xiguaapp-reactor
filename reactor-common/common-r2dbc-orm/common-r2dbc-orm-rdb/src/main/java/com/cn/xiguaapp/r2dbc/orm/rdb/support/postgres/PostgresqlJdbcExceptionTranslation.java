package com.cn.xiguaapp.r2dbc.orm.rdb.support.postgres;


import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ExceptionTranslation;

/**
 * @author xiguaapp
 */
public class PostgresqlJdbcExceptionTranslation implements ExceptionTranslation {


    @Override
    public Throwable translate(Throwable e) {


        return e;
    }
}
