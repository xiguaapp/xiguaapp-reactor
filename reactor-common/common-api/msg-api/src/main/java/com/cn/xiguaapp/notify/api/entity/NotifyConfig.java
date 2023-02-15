package com.cn.xiguaapp.notify.api.entity;

import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc 消息通知配置
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(value = "notify_config")
@ApiModel(value = "NotifyConfig",description = "消息通知")
public class NotifyConfig extends SuperEntity implements Serializable {
    /**
     * 配置名称
     */
    @Column
//    @Schema(description = "配置名称")
    private String name;

    /**
     * 通知类型
     */
    @Column
//    @Schema(description = "通知类型")
    private String type;

    /**
     * 服务提供商
     */
    @Column
//    @Schema(description = "服务提供商")
    private String provider;

    /**
     * 描述
     */
    @Column
//    @Schema(description = "描述")
    private String description;

    /**
     * 配置详情
     */
    @Column
//    @JsonFormat
//    @ColumnType(jdbcType = JDBCType.CLOB)
//    @Schema(description = "配置信息")
    private Map<String, Object> configuration;
    public NotifierProperties toProperties() {
        NotifierProperties properties = new NotifierProperties();
        properties.setProvider(provider);
        properties.setId(getId());
        properties.setType(type);
        properties.setConfiguration(configuration == null ? new HashMap<>() : configuration);
        properties.setName(name);
        return properties;
    }
}
