package com.cn.xiguaapp.pay.baidu.constant;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.xiguaapp.java.common.core.base.BillType;
import com.cn.xiguaapp.xiguaapp.java.common.utils.DateUtils;

/**
 * @author xiguaapp
 * @desc 百度账单类型
 * @since 1.0 20:43
 */
public class BaiduBillType implements BillType {
    /**
     * 用户accessToken
     */
    private String accessToken;
    /**

     *  值为DOWNLOAD_ORDER_BILL与DOWNLOAD_BILL
     *  com.egzosn.pay.baidu.bean.BaiduTransactionType#DOWNLOAD_ORDER_BILL
     *  com.egzosn.pay.baidu.bean.BaiduTransactionType#DOWNLOAD_BILL
     */
    private String type;

    private String datePattern;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取类型名称
     *
     * @return 类型
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * 获取类型对应的日期格式化表达式
     *
     * @return 日期格式化表达式
     */
    @Override
    public String getDatePattern() {
        if (StringUtils.isEmpty(datePattern)){
            datePattern = DateUtils.YYYY_MM_DD;
        }
        return datePattern;
    }

    /**
     * 获取文件类型
     *
     * @return 文件类型
     */
    @Override
    public String getFileType() {
        return null;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }



    /**
     * 自定义属性
     *
     * @return 自定义属性
     */
    @Override
    public String getCustom() {
        return accessToken;
    }

    public BaiduBillType() {
    }

    public BaiduBillType(String accessToken) {
        this.accessToken = accessToken;
    }

    public BaiduBillType(String accessToken, String type) {
        this.accessToken = accessToken;
        this.type = type;
    }
}
