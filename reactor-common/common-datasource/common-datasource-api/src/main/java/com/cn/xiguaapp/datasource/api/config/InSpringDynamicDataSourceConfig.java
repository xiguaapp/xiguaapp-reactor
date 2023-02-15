package com.cn.xiguaapp.datasource.api.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiguaapp
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InSpringDynamicDataSourceConfig extends DynamicDataSourceConfig {
    private static final long serialVersionUID = -8434216403009495774L;

    private String beanName;
}
