package com.cn.xiguaapp.thrid.auth.request.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.thrid.auth.cache.AuthStateCache;
import com.cn.xiguaapp.thrid.auth.config.AuthConfig;
import com.cn.xiguaapp.thrid.auth.config.AuthDefaultSource;
import com.cn.xiguaapp.thrid.auth.constants.AuthUserGender;
import com.cn.xiguaapp.thrid.auth.constants.scope.impl.AuthGiteeScope;
import com.cn.xiguaapp.thrid.auth.exception.AuthException;
import com.cn.xiguaapp.thrid.auth.model.AuthUser;
import com.cn.xiguaapp.thrid.auth.model.callback.AuthCallback;
import com.cn.xiguaapp.thrid.auth.model.token.AuthToken;
import com.cn.xiguaapp.thrid.auth.utils.AuthScopeUtils;
import com.cn.xiguaapp.thrid.auth.utils.UrlBuilder;

/**
 * @author xiguaapp
 * @desc Gitee登录
 * @since 1.0 22:27
 */
public class AuthGiteeRequest extends AuthDefaultRequest {

    public AuthGiteeRequest(AuthConfig config) {
        super(config, AuthDefaultSource.GITEE);
    }

    public AuthGiteeRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.GITEE, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        this.checkResponse(accessTokenObject);
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .refreshToken(accessTokenObject.getString("refresh_token"))
                .scope(accessTokenObject.getString("scope"))
                .tokenType(accessTokenObject.getString("token_type"))
                .expireIn(accessTokenObject.getIntValue("expires_in"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String userInfo = doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(userInfo);
        this.checkResponse(object);
        return AuthUser.builder()
                .rawUserInfo(object)
                .uuid(object.getString("id"))
                .username(object.getString("login"))
                .avatar(object.getString("avatar_url"))
                .blog(object.getString("blog"))
                .nickname(object.getString("name"))
                .company(object.getString("company"))
                .location(object.getString("address"))
                .email(object.getString("email"))
                .remark(object.getString("bio"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("scope", this.getScopes(" ", true, AuthScopeUtils.getDefaultScopes(AuthGiteeScope.values())))
                .build();
    }
}
