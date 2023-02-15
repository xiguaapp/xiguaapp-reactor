package com.cn.xiguaapp.notify.api.constant;

import com.cn.xiguapp.common.core.utils.EnumDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc 订阅状态
 */
@Getter
@AllArgsConstructor
public enum SubscribeState implements EnumDict<String> {
    enabled("订阅中"),
    disabled("已停止");
    private String text;

    @Override
    public String getValue() {
        return name();
    }
}
