package com.cn.xiguaapp.notify.api.entity;

import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import com.cn.xiguaapp.notify.api.constant.SubscribeState;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import java.util.Map;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(value = "notify_subscribers")
@ApiModel(value = "NotifySubscriber",description = "消息订阅")
public class NotifySubscriber extends SuperEntity {
    private static final long serialVersionUID = -1L;

//    @Comment("订阅者类型,如:user")
    @Column(length = 32, nullable = false, updatable = false)
//    @Hidden
    private String subscriberType;

//    @Comment("订阅者ID")
    @Column(length = 32, nullable = false, updatable = false)
//    @Hidden
    private String subscriber;

//    @Comment("主题提供商标识,如:device_alarm")
    @Column(length = 32, nullable = false, updatable = false)
//    @Schema(description = "主题标识,如:device_alarm")
    private String topicProvider;

//    @Comment("订阅名称")
    @Column(length = 64, nullable = false)
//    @Schema(description = "订阅名称")
    private String subscribeName;

//    @Comment("主题名称,如:设备告警")
    @Column(length = 64, nullable = false)
//    @Schema(description = "主题名称")
    private String topicName;

//    @Comment("主题订阅配置")
    @Column(length = 3000)
//    @JsonCodec
//    @ColumnType
//    @Schema(description = "订阅配置,根据主题标识不同而不同.")
    private Map<String, Object> topicConfig;

    @Column
//    @Comment("描述")
//    @Schema(description = "说明")
    private String description;

//    @Comment("状态:enabled,disabled")
    @Column(length = 32)
//    @EnumCodec
//    @ColumnType(javaType = String.class)
//    @DefaultValue("enabled")
//    @Schema(description = "状态.")
    private SubscribeState state;
}
