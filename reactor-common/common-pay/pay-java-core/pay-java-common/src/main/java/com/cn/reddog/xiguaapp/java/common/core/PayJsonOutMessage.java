package com.cn.xiguaapp.xiguaapp.java.common.core;

import lombok.NoArgsConstructor;

/**
 * @author xiguaapp
 * @desc 支付回调返回json信息
 * @since 1.0 14:08
 */
@NoArgsConstructor
public class PayJsonOutMessage extends PayOutMessage {

    @Override
    public String toMessage() {
        return getContent();
    }
}
