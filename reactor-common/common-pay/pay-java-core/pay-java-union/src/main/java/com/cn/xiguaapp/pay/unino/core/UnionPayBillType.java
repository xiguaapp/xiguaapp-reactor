package com.cn.xiguaapp.pay.unino.core;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.BillType;

/**
 * @author xiguaapp
 * @desc 银联账单类型
 * @since 1.0 21:42
 */
public class UnionPayBillType implements BillType {

    private String fileType = "00";

    /**
     * 获取类型名称
     *
     * @return 类型
     */
    @Override
    public String getType() {
        return null;
    }

    /**
     * 获取类型对应的日期格式化表达式
     *
     * @return 日期格式化表达式
     */
    @Override
    public String getDatePattern() {
        return null;
    }

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    @Override
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 自定义属性
     *
     * @return 自定义属性
     */
    @Override
    public String getCustom() {
        return null;
    }

    public UnionPayBillType() {
    }

    public UnionPayBillType(String fileType) {
        if (StringUtils.isNotEmpty(fileType)) {
            this.fileType = fileType;
        }
    }
}