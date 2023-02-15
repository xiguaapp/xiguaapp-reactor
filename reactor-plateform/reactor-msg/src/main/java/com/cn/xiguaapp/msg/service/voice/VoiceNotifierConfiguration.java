package com.cn.xiguaapp.msg.service.voice;

import com.cn.xiguaapp.msg.service.voice.aliyun.AliyunNotifierProvider;
import com.cn.xiguaapp.notify.api.template.TemplateManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoiceNotifierConfiguration {


    @Bean
    @ConditionalOnBean(TemplateManager.class)
    public AliyunNotifierProvider aliyunNotifierProvider(TemplateManager templateManager) {
        return new AliyunNotifierProvider(templateManager);
    }

}
