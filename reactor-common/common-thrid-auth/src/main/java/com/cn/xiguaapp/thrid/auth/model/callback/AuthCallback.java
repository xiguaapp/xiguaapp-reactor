package com.cn.xiguaapp.thrid.auth.model.callback;

import lombok.*;

import java.io.Serializable;

/**
 * @author xiguaapp
 * @desc 授权回调参数类
 * @since 1.0 21:15
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthCallback implements Serializable {
    /**
     * 访问AuthorizeUrl后回调时带的参数code
     */
    private String code;

    /**
     * 访问AuthorizeUrl后回调时带的参数auth_code，该参数目前只使用于支付宝登录
     */
    private String auth_code;

    /**
     * 访问AuthorizeUrl后回调时带的参数state，用于和请求AuthorizeUrl前的state比较，防止CSRF攻击
     */
    private String state;

    /**
     * 华为授权登录接受code的参数名
     *
     * @since 1.10.0
     */
    private String authorization_code;

    /**
     * Twitter回调后返回的oauth_token
     *
     * @since 1.13.0
     */
    private String oauth_token;

    /**
     * Twitter回调后返回的oauth_verifier
     *
     * @since 1.13.0
     */
    private String oauth_verifier;
}