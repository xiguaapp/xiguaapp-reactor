package com.cn.xiguaapp.notify.api.constant;

import com.cn.xiguapp.common.core.utils.EnumDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/26
 * @desc 消息状态
 */
@AllArgsConstructor
@Getter
public enum NotificationState implements EnumDict<String> {
    read("已读"),
    un_read("未读"),
    ;
    private final String text;

    @Override
    public String getValue() {
        return name();
    }
}
