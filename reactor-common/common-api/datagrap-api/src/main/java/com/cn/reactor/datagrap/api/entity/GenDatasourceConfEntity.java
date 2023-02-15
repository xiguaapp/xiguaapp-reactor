package com.cn.reactor.datagrap.api.entity;

import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/21
 * @desc 数据源配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("system_datasource_conf")
@Accessors(chain = true)
public class GenDatasourceConfEntity extends SuperEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * jdbcurl
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
