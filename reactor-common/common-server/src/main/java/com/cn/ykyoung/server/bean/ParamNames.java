package com.cn.ykyoung.server.bean;

/**
 * 请求参数名定义
 *
 * 参数	            类型	   是否必填	最大长度	    描述	                        示例值
 * app_id	        String	是	    32	    支付宝分配给开发者的应用ID	        2014072300007148
 * method	        String	是	    128	    接口名称	                        alipay.trade.fastpay.refund.query
 * format	        String	否	    40	    仅支持JSON	                    JSON
 * charset	        String	是	    10	    请求使用的编码格式，如utf-8,gbk,gb2312等	utf-8
 * sign_type	    String	是	    10	    商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2	RSA2
 * sign	            String	是	    344	    商户请求参数的签名串，详见签名	详见示例
 * timestamp	    String	是	    19	    发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"	2014-07-24 03:07:50
 * version	        String	是	    3	    调用的接口版本，固定为：1.0	1.0
 * notify_url	    String	否	    256	    支付宝服务器主动通知商户服务器里指定的页面http/https路径。	http://api.test.alipay.net/atinterface/receive_notify.htm
 * app_auth_token	String	否	    40	    详见应用授权概述
 * biz_content	    String	是		        请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
 *
 */
public class ParamNames {
    /** 分配给开发者的应用ID */
    public static String APP_KEY_NAME = "app_id";
    /** 接口名称 */
    public static String API_NAME = "method";
    /** 仅支持JSON */
    public static String FORMAT_NAME = "format";
    /** 请求使用的编码格式 */
    public static String CHARSET_NAME = "charset";
    /** 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2 */
    public static String SIGN_TYPE_NAME = "sign_type";
    /** 商户请求参数的签名串 */
    public static String SIGN_NAME = "sign";
    /** 发送请求的时间 */
    public static String TIMESTAMP_NAME = "timestamp";
    /** 调用的接口版本 */
    public static String VERSION_NAME = "version";
    /** 开放平台主动通知商户服务器里指定的页面http/https路径 */
    public static String NOTIFY_URL_NAME = "notify_url";
    /** OAuth 2.0授权token */
    public static String APP_AUTH_TOKEN_NAME = "app_auth_token";
    /** 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档 */
    public static String BIZ_CONTENT_NAME = "biz_content";

    /** 时间戳格式 */
    public static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String HEADER_VERSION_NAME = "sop-version";


}
