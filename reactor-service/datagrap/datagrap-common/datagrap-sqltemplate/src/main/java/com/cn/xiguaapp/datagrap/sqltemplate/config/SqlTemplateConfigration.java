package com.cn.xiguaapp.datagrap.sqltemplate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiguaapp
 * @date 2021-02-22
 * @desc sql路径配置
 */
@ConfigurationProperties(prefix = SqlTemplateConfigration.PREFIX)
@Data
public class SqlTemplateConfigration {
    public static final String PREFIX="sql.template";
    private String path;
}
