package com.cn.xiguaapp.common.gateway.bean;

import com.cn.xiguaapp.common.gateway.manage.ChannelMsgProcessor;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc 执行加载操作
 */
@FunctionalInterface
public interface BeanInitializer extends ChannelMsgProcessor {
    /**
     * 数据加载操作
     */
    void load();
}
