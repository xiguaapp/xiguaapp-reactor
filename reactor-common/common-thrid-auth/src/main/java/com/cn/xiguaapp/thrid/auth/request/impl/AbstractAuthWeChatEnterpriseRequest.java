package com.cn.xiguaapp.thrid.auth.request.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.thrid.auth.cache.AuthStateCache;
import com.cn.xiguaapp.thrid.auth.config.AuthConfig;
import com.cn.xiguaapp.thrid.auth.config.AuthSource;
import com.cn.xiguaapp.thrid.auth.constants.AuthResponseStatus;
import com.cn.xiguaapp.thrid.auth.constants.AuthUserGender;
import com.cn.xiguaapp.thrid.auth.exception.AuthException;
import com.cn.xiguaapp.thrid.auth.model.AuthUser;
import com.cn.xiguaapp.thrid.auth.model.callback.AuthCallback;
import com.cn.xiguaapp.thrid.auth.model.token.AuthToken;
import com.cn.xiguaapp.thrid.auth.utils.HttpUtils;
import com.cn.xiguaapp.thrid.auth.utils.UrlBuilder;

/**
 * @author xiguaapp
 * @desc 企业微信登录父类
 * @since 1.0 22:10
 */
public abstract class AbstractAuthWeChatEnterpriseRequest extends AuthDefaultRequest {

    public AbstractAuthWeChatEnterpriseRequest(AuthConfig config, AuthSource source) {
        super(config,source);
    }


    public AbstractAuthWeChatEnterpriseRequest(AuthConfig config, AuthSource source, AuthStateCache authStateCache) {
        super(config, source, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doGetAuthorizationCode(accessTokenUrl(authCallback.getCode()));

        JSONObject object = this.checkResponse(response);

        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .expireIn(object.getIntValue("expires_in"))
                .code(authCallback.getCode())
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String response = doGetUserInfo(authToken);
        JSONObject object = this.checkResponse(response);

        // 返回 OpenId 或其他，均代表非当前企业用户，不支持
        if (!object.containsKey("UserId")) {
            throw new AuthException(AuthResponseStatus.UNIDENTIFIED_PLATFORM, source);
        }
        String userId = object.getString("UserId");
        String userDetailResponse = getUserDetail(authToken.getAccessToken(), userId);
        JSONObject userDetail = this.checkResponse(userDetailResponse);

        return AuthUser.builder()
                .rawUserInfo(userDetail)
                .username(userDetail.getString("name"))
                .nickname(userDetail.getString("alias"))
                .avatar(userDetail.getString("avatar"))
                .location(userDetail.getString("address"))
                .email(userDetail.getString("email"))
                .uuid(userId)
                .gender(AuthUserGender.getWechatRealGender(userDetail.getString("gender")))
                .token(authToken)
                .source(source.toString())
                .build();
    }

    /**
     * 校验请求结果
     *
     * @param response 请求结果
     * @return 如果请求结果正常，则返回JSONObject
     */
    private JSONObject checkResponse(String response) {
        JSONObject object = JSONObject.parseObject(response);

        if (object.containsKey("errcode") && object.getIntValue("errcode") != 0) {
            throw new AuthException(object.getString("errmsg"), source);
        }

        return object;
    }


    /**
     * 返回获取accessToken的url
     *
     * @param code 授权码
     * @return 返回获取accessToken的url
     */
    @Override
    protected String accessTokenUrl(String code) {
        return UrlBuilder.fromBaseUrl(source.accessToken())
                .queryParam("corpid", config.getClientId())
                .queryParam("corpsecret", config.getClientSecret())
                .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken 用户授权后的token
     * @return 返回获取userInfo的url
     */
    @Override
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo())
                .queryParam("access_token", authToken.getAccessToken())
                .queryParam("code", authToken.getCode())
                .build();
    }

    /**
     * 用户详情
     *
     * @param accessToken accessToken
     * @param userId      企业内用户id
     * @return 用户详情
     */
    private String getUserDetail(String accessToken, String userId) {
        String userDetailUrl = UrlBuilder.fromBaseUrl("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .queryParam("access_token", accessToken)
                .queryParam("userid", userId)
                .build();
        return new HttpUtils(config.getHttpConfig()).get(userDetailUrl);
    }

}