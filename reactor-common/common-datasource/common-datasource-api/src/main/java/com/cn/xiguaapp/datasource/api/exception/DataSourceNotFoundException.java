package com.cn.xiguaapp.datasource.api.exception;


/**
 * @author xiguaapp
 */
public class DataSourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8750742814977236806L;
    private String dataSourceId;

    public DataSourceNotFoundException(String dataSourceId) {
        this(dataSourceId, dataSourceId);
    }

    public DataSourceNotFoundException(String dataSourceId, String message) {
        super(message);
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

}
