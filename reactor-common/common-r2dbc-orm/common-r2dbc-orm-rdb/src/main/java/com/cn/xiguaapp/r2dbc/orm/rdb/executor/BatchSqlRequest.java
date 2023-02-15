package com.cn.xiguaapp.r2dbc.orm.rdb.executor;

import java.util.List;

/**
 * @author xiguaapp
 */
public interface BatchSqlRequest extends SqlRequest {

    List<SqlRequest> getBatch();

}
