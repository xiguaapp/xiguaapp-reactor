package com.cn.xiguaapp.third.auth.test.request;

import com.cn.xiguaapp.thrid.auth.config.AuthConfig;
import com.cn.xiguaapp.thrid.auth.request.AuthRequest;
import com.cn.xiguaapp.thrid.auth.request.impl.AuthWeChatMpRequest;
import com.cn.xiguaapp.thrid.auth.utils.AuthStateUtils;
import org.junit.Test;

public class AuthWeChatMpRequestTest {

    @Test
    public void authorize() {

        AuthRequest request = new AuthWeChatMpRequest(AuthConfig.builder()
            .clientId("a")
            .clientSecret("a")
            .redirectUri("https://www.justauth.cn")
            .build());
        System.out.println(request.authorize(AuthStateUtils.createState()));
    }
}
