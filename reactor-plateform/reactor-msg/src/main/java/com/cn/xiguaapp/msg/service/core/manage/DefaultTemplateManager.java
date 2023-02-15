package com.cn.xiguaapp.msg.service.core.manage;

import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguaapp.notify.api.template.AbstractTemplateManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/27
 * @desc
 */
@Component
public class DefaultTemplateManager extends AbstractTemplateManager implements BeanPostProcessor{
    @Override
    protected Mono<TemplateProperties> getProperties(NotifyType type, String id) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
