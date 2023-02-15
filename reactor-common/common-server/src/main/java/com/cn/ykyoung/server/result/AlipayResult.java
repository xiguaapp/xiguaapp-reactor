package com.cn.ykyoung.server.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/22
 * @desc
 */
@Data
@Accessors(chain = true)
public class AlipayResult {
    private String sub_code;
    private String sub_msg;
}
