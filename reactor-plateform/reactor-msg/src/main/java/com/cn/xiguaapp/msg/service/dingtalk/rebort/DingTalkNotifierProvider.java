/*
 *     Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/19 下午9:44 >
 *
 *     Send: 1125698980@qq.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.msg.service.dingtalk.rebort;

import com.alibaba.fastjson.JSON;
import com.cn.xiguapp.common.core.utils.BeansUtils;
import com.cn.xiguapp.common.core.utils.ValidatorUtils;
import com.cn.xiguaapp.msg.service.dingtalk.DingTalkMessageTemplate;
import com.cn.xiguaapp.msg.service.dingtalk.DingTalkNotifier;
import com.cn.xiguaapp.msg.service.dingtalk.DingTalkProperties;
import com.cn.xiguaapp.msg.service.dingtalk.DingTalkProvider;
import com.cn.xiguaapp.notify.api.constant.DefaultNotifyType;
import com.cn.xiguaapp.notify.api.constant.NotifyType;
import com.cn.xiguaapp.notify.api.core.TemplateProperties;
import com.cn.xiguaapp.notify.api.param.NotifierProperties;
import com.cn.xiguaapp.notify.api.template.*;
import com.cn.xiguapp.common.core.core.config.ConfigMetadata;
import com.cn.xiguapp.common.core.core.config.DefaultConfigMetadata;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * @author xiguaapp
 */
@Component
public class DingTalkNotifierProvider implements NotifierProvider, TemplateProvider {

    private WebClient client = WebClient.create();

    private final TemplateManager templateManager;

    public DingTalkNotifierProvider(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public static final DefaultConfigMetadata notifierConfig = new DefaultConfigMetadata("通知配置", "")
        .add("appKey", "appKey", "")
        .add("appSecret", "appSecret", "");

    public static final DefaultConfigMetadata templateConfig = new DefaultConfigMetadata("模版配置", "")
        .add("agentId", "应用ID", "")
        .add("userIdList", "收信人ID", "与部门ID不能同时为空")
        .add("departmentIdList", "收信部门ID", "与收信人ID不能同时为空")
        .add("toAllUser", "全部用户", "推送到全部用户")
        .add("message", "内容", "最大不超过500字");

    @Nonnull
    @Override
    public NotifyType getType() {
        return DefaultNotifyType.dingTalk;
    }

    @Nonnull
    @Override
    public Provider getProvider() {
        return DingTalkProvider.dingTalkMessage;
    }

    @Override
    public Mono<DingTalkMessageTemplate> createTemplate(TemplateProperties properties) {
        return Mono.fromSupplier(() -> ValidatorUtils.tryValidate(JSON.parseObject(properties.getTemplate(), DingTalkMessageTemplate.class)));
    }

    @Nonnull
    @Override
    public Mono<DingTalkNotifier> createNotifier(@Nonnull NotifierProperties properties) {
        return Mono.defer(() -> {
            DingTalkProperties dingTalkProperties = BeansUtils.mapToPojo(properties.getConfiguration(),DingTalkProperties.class);
//                    FastBeanCopier.copy(properties.getConfiguration(), new DingTalkProperties());
            return Mono.just(new DingTalkNotifier(properties.getId(), client, ValidatorUtils.tryValidate(dingTalkProperties), templateManager));
        });
    }

    /**
     * 获取通知配置
     * @return
     */
    @Override
    public ConfigMetadata getNotifierConfigMetadata() {
        return notifierConfig;
    }

    /**
     * 获取模板配置
     * @return
     */
    @Override
    public ConfigMetadata getTemplateConfigMetadata() {
        return templateConfig;
    }
}
