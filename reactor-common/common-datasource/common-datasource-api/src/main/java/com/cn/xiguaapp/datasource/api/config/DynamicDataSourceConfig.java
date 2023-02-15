package com.cn.xiguaapp.datasource.api.config;

import com.cn.xiguaapp.datasource.api.constant.DatabaseType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiguaapp
 */
@Data
public class DynamicDataSourceConfig implements Serializable {
    private static final long serialVersionUID = 2776152081818934459L;

    private String id;

    private String name;

    private String describe;

    private DatabaseType databaseType;
}
