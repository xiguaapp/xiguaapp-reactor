package com.cn.xiguaapp.thrid.auth.request.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.thrid.auth.cache.AuthStateCache;
import com.cn.xiguaapp.thrid.auth.config.AuthConfig;
import com.cn.xiguaapp.thrid.auth.config.AuthDefaultSource;
import com.cn.xiguaapp.thrid.auth.constants.AuthToutiaoErrorCode;
import com.cn.xiguaapp.thrid.auth.constants.AuthUserGender;
import com.cn.xiguaapp.thrid.auth.exception.AuthException;
import com.cn.xiguaapp.thrid.auth.model.AuthUser;
import com.cn.xiguaapp.thrid.auth.model.callback.AuthCallback;
import com.cn.xiguaapp.thrid.auth.model.token.AuthToken;
import com.cn.xiguaapp.thrid.auth.utils.UrlBuilder;

/**
 * 今日头条登录
 *
 * @author xiguaapp
 * @since 1.6.0-beta
 */
public class AuthToutiaoRequest extends AuthDefaultRequest {

    public AuthToutiaoRequest(AuthConfig config) {
        super(config, AuthDefaultSource.TOUTIAO);
    }

    public AuthToutiaoRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.TOUTIAO, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doGetAuthorizationCode(authCallback.getCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response);

        this.checkResponse(accessTokenObject);

        return AuthToken.builder()
            .accessToken(accessTokenObject.getString("access_token"))
            .expireIn(accessTokenObject.getIntValue("expires_in"))
            .openId(accessTokenObject.getString("open_id"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String userResponse = doGetUserInfo(authToken);

        JSONObject userProfile = JSONObject.parseObject(userResponse);

        this.checkResponse(userProfile);

        JSONObject user = userProfile.getJSONObject("data");

        boolean isAnonymousUser = user.getIntValue("uid_type") == 14;
        String anonymousUserName = "匿名用户";

        return AuthUser.builder()
            .rawUserInfo(user)
            .uuid(user.getString("uid"))
            .username(isAnonymousUser ? anonymousUserName : user.getString("screen_name"))
            .nickname(isAnonymousUser ? anonymousUserName : user.getString("screen_name"))
            .avatar(user.getString("avatar_url"))
            .remark(user.getString("description"))
            .gender(AuthUserGender.getRealGender(user.getString("gender")))
            .token(authToken)
            .source(source.toString())
            .build();
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     * @since 1.9.3
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(source.authorize())
            .queryParam("response_type", "code")
            .queryParam("client_key", config.getClientId())
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("auth_only", 1)
            .queryParam("display", 0)
            .queryParam("state", getRealState(state))
            .build();
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
            .queryParam("code", code)
            .queryParam("client_key", config.getClientId())
            .queryParam("client_secret", config.getClientSecret())
            .queryParam("grant_type", "authorization_code")
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
            .queryParam("client_key", config.getClientId())
            .queryParam("access_token", authToken.getAccessToken())
            .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("error_code")) {
            throw new AuthException(AuthToutiaoErrorCode.getErrorCode(object.getIntValue("error_code")).getDesc());
        }
    }
}
