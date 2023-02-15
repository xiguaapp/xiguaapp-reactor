package com.cn.xiguaapp.r2dbc.orm.rdb.exception;

import com.cn.xiguaapp.r2dbc.orm.rdb.metadata.RDBColumnMetadata;
import lombok.Getter;

import java.util.List;

/**
 * @author xiguaapp
 */
@Getter
public class DuplicateKeyException extends RuntimeException {

    private boolean primaryKey;

    private List<RDBColumnMetadata> columns;

    public DuplicateKeyException(boolean primaryKey,
                                 List<RDBColumnMetadata> columns,
                                 Throwable cause) {
        super(cause);
        this.primaryKey = primaryKey;
        this.columns = columns;

    }

}
