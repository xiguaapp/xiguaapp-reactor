package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBDatabaseMetadata;

/**
 * @author xiguaapp
 */
public interface DatabaseOperator {

    RDBDatabaseMetadata getMetadata();

    DMLOperator dml();

    DDLOperator ddl();

    SQLOperator sql();

}
