package com.cn.xiguaapp.pay.zdemo.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author xiguaapp
 * @desc 支付账户
 * @since 1.0 22:37
 */
@Data
@ToString
public class ApyAccount {
    // 支付账号id
//    @Id
//    @GeneratedValue
//    @Column(name = "pay_id")
    private Integer payId;
    // 支付合作id,商户id，差不多是支付平台的账号或id
//    @Column(name = "partner")
    private String partner;
    // 应用id
//    @Column(name = "appid")
    private String appid;
    //   支付平台公钥(签名校验使用)，sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
    private String publicKey;
    // 应用私钥(生成签名)
//    @Column(name = "private_key")
    private String privateKey;
    // 异步回调地址
//    @Column(name = "notify_url")
    private String notifyUrl;
    // 同步回调地址
//    @Column(name = "return_url")
    private String returnUrl;
    // 收款账号
//    @Column(name = "seller")
    private String seller;

    //请求证书地址，请使用绝对路径
//    @Column(name = "keystore_path")
    private String keystorePath;
    //证书对应的密码
//    @Column(name = "store_password")
    private String storePassword;


    // 签名类型
//    @Column(name = "sign_type")
    private String signType;
    // 编码类型 枚举值，字符编码 utf-8,gbk等等
//    @Column(name = "input_charset")
    private String inputCharset;
    //支付类型,aliPay：支付宝，wxPay：微信, youdianPay: 友店微信,此处开发者自定义对应com.egzosn.pay.demo.entity.PayType枚举值
//    @Enumerated(EnumType.STRING)
//    @Column(name = "pay_type")
    private PayType payType;
    //是否为测试环境
    private boolean isTest = false;
}
