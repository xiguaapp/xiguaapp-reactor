package com.cn.ykyoung.server.bean;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static com.cn.ykyoung.server.bean.ParamNames.*;

/**
 * 获取开放平台请求参数。
 *
 */
public interface OpenContext {

    /**
     * 返回所有的请求参数
     *
     * @return 返回所有的请求参数
     */
    Map<String, Object> getParameterMap();

    /**
     * 返回业务对象
     *
     * @return 业务对象
     */
    Object getBizObject();


    /**
     * 获取某个参数值
     *
     * @param name 参数名称
     * @return 没有返回null
     */
    default String getParameter(String name) {
        Object value = getParameterMap().get(name);
        return value == null ? null : value.toString();
    }

    /**
     * 返回appid
     *
     * @return 返回appId
     */
    default String getAppId() {
        return getParameter(APP_KEY_NAME);
    }

    /**
     * 返回业务参数json字符串
     *
     * @return 返回字符串业务参数
     */
    default String getBizContent() {
        return getParameter(BIZ_CONTENT_NAME);
    }


    /**
     * 返回字符编码
     *
     * @return 如UTF-8
     */
    default String getCharset() {
        return getParameter(CHARSET_NAME);
    }

    /**
     * 返回接口名
     *
     * @return 如：alipay.goods.get
     */
    default String getMethod() {
        return getParameter(API_NAME);
    }

    /**
     * 返回版本号
     *
     * @return 如：1.0
     */
    default String getVersion() {
        return getParameter(VERSION_NAME);
    }

    /**
     * 返回参数格式化
     *
     * @return 如：json
     */
    default String getFormat() {
        return getParameter(FORMAT_NAME);
    }

    /**
     * 返回签名类型
     *
     * @return 如：RSA2
     */
    default String getSignType() {
        return getParameter(SIGN_TYPE_NAME);
    }

    /**
     * 返回时间戳
     *
     * @return
     */
    default Date getTimestamp() {
        String timestampStr = getParameter(TIMESTAMP_NAME);
        try {
            return DateUtils.parseDate(timestampStr, TIMESTAMP_PATTERN);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 返回token
     *
     * @return 返回token
     */
    default String getAppAuthToken() {
        return getParameter(APP_AUTH_TOKEN_NAME);
    }

    /**
     * 返回回调地址
     *
     * @return 返回回调地址
     */
    default String getNotifyUrl() {
        return getParameter(NOTIFY_URL_NAME);
    }

}
