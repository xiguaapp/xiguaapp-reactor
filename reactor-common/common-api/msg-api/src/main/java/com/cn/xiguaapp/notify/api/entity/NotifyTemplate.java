package com.cn.xiguaapp.notify.api.entity;

import com.cn.xiguaapp.common.r2dbc.starter.deprecated.r2dbc.entity.SuperEntity;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;

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
@Table(value = "notify_template")
@ApiModel(value = "NotifyTemplate",description = "通知模板")
public class NotifyTemplate extends SuperEntity {
    @Column
//    @Comment("通知类型")
//    @Schema(description = "通知类型ID")
    private String type;

    @Column
//    @Comment("通知服务商")
//    @Schema(description = "通知服务商ID")
    private String provider;

    @Column
//    @Comment("模板名称")
//    @Schema(description = "模版名称")
    private String name;

//    @Comment("模板内容")
    @Column
//    @ColumnType(jdbcType = JDBCType.CLOB)
//    @Schema(description = "模版内容(根据服务商不同而不同)")
    private String template;


    public TemplateProperties toTemplateProperties() {
        TemplateProperties properties = new TemplateProperties();
        properties.setProvider(provider);
        properties.setType(type);
        properties.setTemplate(template);
        return properties;
    }
}
