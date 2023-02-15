package com.cn.xiguaapp.common.oauth.core.constants;

/**
 * @author xiguaapp
 * @desc 注解鉴权的验证模式
 * @since
 */
public enum SaMode {

    /**
     * 必须具有所有的选项
     */
    AND,

    /**
     * 只需具有其中一个选项
     */
    OR

}