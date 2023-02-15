package com.cn.xiguaapp.notify.api.entity;

import com.alibaba.fastjson.JSON;
import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import com.cn.xiguaapp.notify.api.constant.NotifyState;
import com.cn.xiguaapp.notify.api.core.SerializableNotifierEvent;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "notify_history")
@ApiModel(value = "NotifyHistory",description = "消息通知记录")
public class NotifyHistory extends SuperEntity {
//    private static final long serialVersionUID = -6849794470754667710L;

//    @Column(nullable = false)
//    @Schema(description = "状态")
    private NotifyState state;

//    @Schema(description = "错误类型")
    @Column("error_type")
    private String errorType;

//    @Schema(description = "异常栈")
    @Column("error_stack")
    private String errorStack;

//    @DefaultValue("-")
//    @Schema(description = "模版ID")
    @Column("template_id")
    private String templateId;

//    @Column
//    @ColumnType(jdbcType = JDBCType.CLOB, javaType = String.class)
//    @Schema(description = "模版内容")
    private String template;

//    @Column
//    @ColumnType(jdbcType = JDBCType.CLOB, javaType = String.class)
//    @JsonCodec
//    @Schema(description = "上下文")
    private Map<String, Object> context;

//    @Column(length = 32, nullable = false)
//    @Schema(description = "服务商")
    private String provider;

//    @Schema(description = "通知类型")
    @Column("notify_type")
    private String notifyType;

//    @DefaultValue(generator = Generators.CURRENT_TIME)
//    @Schema(description = "通知时间")
    @Column("notify_time")
    private LocalDateTime notifyTime;

//    @DefaultValue("0")
    //    @Schema(description = "重试次数")
    @Column("retry_times")
    private Integer retryTimes;

    public static NotifyHistory of(SerializableNotifierEvent event) {
        NotifyHistory entity = BeansUtils.copy(event, new NotifyHistory());
        if (null != event.getTemplate()) {
            entity.setTemplate(JSON.toJSONString(event.getTemplate()));
        }
        if (event.isSuccess()) {
            entity.setState(NotifyState.success);
        } else {
            entity.setErrorStack(event.getCause());
            entity.setState(NotifyState.error);
        }
        return entity;
    }

}
