package com.cn.xiguaapp.common.gateway.result;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
public interface DataNameBuilder {
    /**
     * 构建数据节点名称
     * @param method
     * @return
     */
    String build(String method);
}
