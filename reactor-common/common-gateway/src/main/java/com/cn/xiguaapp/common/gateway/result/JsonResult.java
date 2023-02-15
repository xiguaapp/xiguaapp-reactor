package com.cn.xiguaapp.common.gateway.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JsonResult extends ApiResult {
    private Object data;
}
