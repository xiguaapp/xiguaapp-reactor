package com.cn.xiguaapp.notify.api.constant;

import com.cn.xiguapp.common.core.utils.EnumDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc 通知状态
 */
@Getter
@AllArgsConstructor
public enum NotifyState implements EnumDict<String> {
    success("成功"),
    error("失败"),
    ;
    private final String text;

    @Override
    public String getValue() {
        return name();
    }
}
