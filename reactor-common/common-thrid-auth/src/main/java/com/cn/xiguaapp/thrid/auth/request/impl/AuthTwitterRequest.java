package com.cn.xiguaapp.thrid.auth.request.impl;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.constants.Constants;
import com.xkcoding.http.support.HttpHeader;
import com.xkcoding.http.util.MapUtil;
import com.cn.xiguaapp.thrid.auth.cache.AuthStateCache;
import com.cn.xiguaapp.thrid.auth.config.AuthConfig;
import com.cn.xiguaapp.thrid.auth.model.AuthUser;
import com.cn.xiguaapp.thrid.auth.model.callback.AuthCallback;
import com.cn.xiguaapp.thrid.auth.model.token.AuthToken;
import com.cn.xiguaapp.thrid.auth.utils.GlobalAuthUtils;
import com.cn.xiguaapp.thrid.auth.utils.HttpUtils;
import com.cn.xiguaapp.thrid.auth.utils.UrlBuilder;


import java.util.HashMap;
import java.util.Map;

import static com.cn.xiguaapp.thrid.auth.config.AuthDefaultSource.TWITTER;
import static com.cn.xiguaapp.thrid.auth.utils.GlobalAuthUtils.generateTwitterSignature;

/**
 * Twitter登录
 *
 * @author xiguaapp
 * @since 1.13.0
 */
public class AuthTwitterRequest extends AuthDefaultRequest {

    private static final String PREAMBLE = "OAuth";

    public AuthTwitterRequest(AuthConfig config) {
        super(config, TWITTER);
    }

    public AuthTwitterRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, TWITTER, authStateCache);
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
        AuthToken token  = this.getRequestToken();
        return UrlBuilder.fromBaseUrl(source.authorize())
            .queryParam("oauth_token", token.getOauthToken())
            .build();
    }

    /**
     * Obtaining a request token
     * https://developer.twitter.com/en/docs/twitter-for-websites/log-in-with-twitter/guides/implementing-sign-in-with-twitter
     *
     * @return request token
     */
    public AuthToken getRequestToken() {
        String baseUrl = "https://api.twitter.com/oauth/request_token";

        Map<String, String> oauthParams = buildOauthParams();
        oauthParams.put("oauth_callback", config.getRedirectUri());
        oauthParams.put("oauth_signature", generateTwitterSignature(oauthParams, "POST", baseUrl, config.getClientSecret(), null));
        String header = buildHeader(oauthParams);

        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Authorization", header);
        httpHeader.add("User-Agent", "themattharris' HTTP Client");
        httpHeader.add("Host", "api.twitter.com");
        httpHeader.add("Accept", "*/*");
        String requestToken = new HttpUtils(config.getHttpConfig()).post(baseUrl, null, httpHeader);

        Map<String, String> res = MapUtil.parseStringToMap(requestToken, false);

        return AuthToken.builder()
            .oauthToken(res.get("oauth_token"))
            .oauthTokenSecret(res.get("oauth_token_secret"))
            .oauthCallbackConfirmed(Boolean.valueOf(res.get("oauth_callback_confirmed")))
            .build();
    }

    /**
     * Convert request token to access token
     * https://developer.twitter.com/en/docs/twitter-for-websites/log-in-with-twitter/guides/implementing-sign-in-with-twitter
     *
     * @return access token
     */
    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        Map<String, String> oauthParams = buildOauthParams();
        oauthParams.put("oauth_token", authCallback.getOauth_token());
        oauthParams.put("oauth_verifier", authCallback.getOauth_verifier());
        oauthParams.put("oauth_signature", generateTwitterSignature(oauthParams, "POST", source.accessToken(), config.getClientSecret(), authCallback
            .getOauth_token()));
        String header = buildHeader(oauthParams);

        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Authorization", header);
        httpHeader.add(Constants.CONTENT_TYPE, "application/x-www-form-urlencoded");

        Map<String, String> form = new HashMap<>(1);
        form.put("oauth_verifier", authCallback.getOauth_verifier());
        String response = new HttpUtils(config.getHttpConfig()).post(source.accessToken(), form, httpHeader, false);

        Map<String, String> requestToken = MapUtil.parseStringToMap(response, false);

        return AuthToken.builder()
            .oauthToken(requestToken.get("oauth_token"))
            .oauthTokenSecret(requestToken.get("oauth_token_secret"))
            .userId(requestToken.get("user_id"))
            .screenName(requestToken.get("screen_name"))
            .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user_id", authToken.getUserId());
        queryParams.put("screen_name", authToken.getScreenName());
        queryParams.put("include_entities", Boolean.toString(true));

        Map<String, String> oauthParams = buildOauthParams();
        oauthParams.put("oauth_token", authToken.getOauthToken());

        Map<String, String> params = new HashMap<>(oauthParams);
        params.putAll(queryParams);
        oauthParams.put("oauth_signature", generateTwitterSignature(params, "GET", source.userInfo(), config.getClientSecret(), authToken
            .getOauthTokenSecret()));
        String header = buildHeader(oauthParams);

        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Authorization", header);
        String response = new HttpUtils(config.getHttpConfig()).get(userInfoUrl(authToken), null, httpHeader, false);
        JSONObject userInfo = JSONObject.parseObject(response);

        return AuthUser.builder()
            .rawUserInfo(userInfo)
            .uuid(userInfo.getString("id_str"))
            .username(userInfo.getString("screen_name"))
            .nickname(userInfo.getString("name"))
            .remark(userInfo.getString("description"))
            .avatar(userInfo.getString("profile_image_url_https"))
            .blog(userInfo.getString("url"))
            .location(userInfo.getString("location"))
            .avatar(userInfo.getString("profile_image_url"))
            .source(source.toString())
            .token(authToken)
            .build();
    }

    @Override
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo())
            .queryParam("user_id", authToken.getUserId())
            .queryParam("screen_name", authToken.getScreenName())
            .queryParam("include_entities", true)
            .build();
    }

    private Map<String, String> buildOauthParams() {
        Map<String, String> params = new HashMap<>(5);
        params.put("oauth_consumer_key", config.getClientId());
        params.put("oauth_nonce", GlobalAuthUtils.generateNonce(32));
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", GlobalAuthUtils.getTimestamp());
        params.put("oauth_version", "1.0");
        return params;
    }

    private String buildHeader(Map<String, String> oauthParams) {
        final StringBuilder sb = new StringBuilder(PREAMBLE + " ");

        for (Map.Entry<String, String> param : oauthParams.entrySet()) {
            sb.append(param.getKey()).append("=\"").append(GlobalAuthUtils.urlEncode(param.getValue())).append('"').append(", ");
        }

        return sb.deleteCharAt(sb.length() - 2).toString();
    }
}