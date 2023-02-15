package com.cn.xiguaapp.msg.service.dingtalk;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xiguaapp
 * @desc 叮叮配置
 */
@Getter
@Setter
public class DingTalkProperties {

    @NotBlank(message = "appKey不能为空")
    private String appKey;

    @NotBlank(message = "appSecret不能为空")
    private String appSecret;


}
