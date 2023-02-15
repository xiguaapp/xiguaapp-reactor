package com.cn.xiguaapp.common.gateway.manage;

import com.cn.xiguaapp.common.gateway.bean.ChannelMsg;

import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc 选择渠道 如新增或者删除
 */
public interface ChannelMsgProcessor {
    /**
     * 根据渠道选择执行某方法
     * @desc  渠道参数
     */
    default Consumer<ChannelMsg> process(){
        return channelMsg -> {};
    }
}
