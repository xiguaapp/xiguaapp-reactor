package com.cn.xiguaapp.common.gateway.result;

import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import lombok.Data;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 默认的结果封装类.
 *  * <pre>
 *  *
 *  * xml返回结果:
 *  * <response>
 *  *     <code>50</code>
 *  *     <msg>Remote com.cn.xiguaapp.msg.service error</msg>
 *  *     <sub_code>isv.invalid-parameter</sub_code>
 *  *     <sub_msg>非法参数</sub_msg>
 *  * </response>
 *  * 成功情况：
 *  * <response>
 *  *     <code>0</code>
 *  *     <msg>成功消息</msg>
 *  *     <data>
 *  *         ...返回内容
 *  *     </data>
 *  * </response>
 *  *
 *  * json返回格式：
 *  * {
 *  *  "code":"50",
 *  * 	"msg":"Remote com.cn.xiguaapp.msg.service error",
 *  * 	"sub_code":"isv.invalid-parameter",
 *  * 	"sub_msg":"非法参数"
 *  * }
 *  * 成功情况：
 *  * {
 *  *  "code":"0",
 *  * 	"msg":"成功消息内容。。。",
 *  * 	"data":{
 *  * 	    ...返回内容
 *  *    }
 *  * }
 *  * </pre>
 *  * <p>
 *  * 字段说明：
 *  * code:网关异常码 <br>
 *  * msg:网关异常信息 <br>
 *  * sub_code:业务异常码 <br>
 *  * sub_msg:业务异常信息 <br>
 *  *
 */
@Data
public class ApiResult implements Result{
    /**
     * 网关异常码，范围0~100 成功返回"0"
     */
    private String code = ErrorEnum.SUCCESS.getErrorMeta().getCode();

    /**
     * 网关异常信息
     */
    private String msg;

    /**
     * 业务异常码
     */
    private String sub_msg;

    /**
     * 业务异常信息
     */
    private String sub_code;

    private String sign;
}
