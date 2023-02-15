package com.cn.xiguaapp.common.gateway.bean;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
public interface Param {
    /**
     * 获取接口名
     * @return 返回接口名
     */
    String fetchName();

    /**
     * 获取版本号
     * @return 返回版本号
     */
    String fetchVersion();

    /**
     * 获取appKey
     * @return 返回appKey
     */
    String fetchAppKey();

    /**
     * 获取业务参数
     * @return 返回业务参数
     */
    String fetchData();

    /**
     * 获取时间戳
     * @return 返回时间戳
     */
    String fetchTimestamp();

    /**
     * 获取签名串
     * @return 返回签名串
     */
    String fetchSign();

    /**
     * 获取格式化类型
     * @return 返回格式化类型
     */
    String fetchFormat();

    /**
     * 获取accessToken
     * @return 返回accessToken
     */
    String fetchAccessToken();

    /**
     * 获取签名方式
     * @return 返回签名方式
     */
    String fetchSignMethod();

    /**
     * 请求使用的编码格式，如utf-8,gbk,gb2312等
     * @return 请求使用的编码格式，如utf-8,gbk,gb2312等
     */
    String fetchCharset();
}
