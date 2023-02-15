package com.cn.ykyoung.server.message;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/10/22
 * @desc 定义错误返回
 *  * sub_code（明细返回码）
 *  * sub_msg（明细返回码描述）
 *  * 解决方案
 */
public interface ServiceError {
    /**
     * sub_code（明细返回码）
     * @return sub_code（明细返回码）
     */
    String getSub_code();

    /**
     * sub_msg（明细返回码描述）
     * @return sub_msg（明细返回码描述）
     */
    String getSub_msg();
}
