package com.cn.xiguaapp.thrid.auth.request.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.xiguaapp.thrid.auth.cache.AuthStateCache;
import com.cn.xiguaapp.thrid.auth.config.AuthConfig;
import com.cn.xiguaapp.thrid.auth.config.AuthDefaultSource;
import com.cn.xiguaapp.thrid.auth.constants.AuthUserGender;
import com.cn.xiguaapp.thrid.auth.constants.scope.impl.AuthGithubScope;
import com.cn.xiguaapp.thrid.auth.exception.AuthException;
import com.cn.xiguaapp.thrid.auth.model.AuthUser;
import com.cn.xiguaapp.thrid.auth.model.callback.AuthCallback;
import com.cn.xiguaapp.thrid.auth.model.token.AuthToken;
import com.cn.xiguaapp.thrid.auth.utils.AuthScopeUtils;
import com.cn.xiguaapp.thrid.auth.utils.GlobalAuthUtils;
import com.cn.xiguaapp.thrid.auth.utils.HttpUtils;
import com.cn.xiguaapp.thrid.auth.utils.UrlBuilder;
import com.xkcoding.http.support.HttpHeader;

import java.util.Map;

/**
 * @author xiguaapp
 * @desc Github登录
 * @since 1.0 22:28
 */
public class AuthGithubRequest extends AuthDefaultRequest {

    public AuthGithubRequest(AuthConfig config) {
        super(config, AuthDefaultSource.GITHUB);
    }

    public AuthGithubRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthDefaultSource.GITHUB, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        Map<String, String> res = GlobalAuthUtils.parseStringToMap(response);

        this.checkResponse(res.containsKey("error"), res.get("error_description"));

        return AuthToken.builder()
                .accessToken(res.get("access_token"))
                .scope(res.get("scope"))
                .tokenType(res.get("token_type"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        HttpHeader header = new HttpHeader();
        header.add("Authorization", "token " + authToken.getAccessToken());
        String response = new HttpUtils(config.getHttpConfig()).get(UrlBuilder.fromBaseUrl(source.userInfo()).build(), null, header, false);
        JSONObject object = JSONObject.parseObject(response);

        this.checkResponse(object.containsKey("error"), object.getString("error_description"));

        return AuthUser.builder()
                .rawUserInfo(object)
                .uuid(object.getString("id"))
                .username(object.getString("login"))
                .avatar(object.getString("avatar_url"))
                .blog(object.getString("blog"))
                .nickname(object.getString("name"))
                .company(object.getString("company"))
                .location(object.getString("location"))
                .email(object.getString("email"))
                .remark(object.getString("bio"))
                .gender(AuthUserGender.UNKNOWN)
                .token(authToken)
                .source(source.toString())
                .build();
    }

    private void checkResponse(boolean error, String errorDescription) {
        if (error) {
            throw new AuthException(errorDescription);
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
                .queryParam("scope", this.getScopes(" ", true, AuthScopeUtils.getDefaultScopes(AuthGithubScope.values())))
                .build();
    }

}
