package com.cn.xiguaapp.notify.api.template;

import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @desc 获取通知类型 服务商 创建模版
 */
public interface TemplateProvider {
    /**
     * 获取通知类型
     * @return
     */
    NotifyType getType();

    /**
     * 获取服务商
     * @return
     */
    Provider getProvider();

    /**
     * 创建模版
     * @param properties
     * @return
     */
    Mono<? extends Template> createTemplate(TemplateProperties properties);

    /**
     * 获取模板配置元数据,通过元数据可以知道此通知所需要的模板配置信息
     * @return
     */
    default ConfigMetadata getTemplateConfigMetadata(){
        return null;
    }
}
