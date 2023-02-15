package com.cn.xiguaapp.r2dbc.orm.rdb.oprator;


import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBTableMetadata;
import com.cn.xiguaapp.r2dbc.orm.rdb.oprator.ddl.TableBuilder;

/**
 * @author xiguaapp
 */
public interface DDLOperator {

    TableBuilder createOrAlter(String name);

    TableBuilder createOrAlter(RDBTableMetadata newTable);

}
