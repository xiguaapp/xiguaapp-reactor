package com.cn.xiguaapp.third.auth.test.model;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.third.auth.test.config.AuthExtendSource;
import com.cn.xiguaapp.thrid.auth.config.AuthDefaultSource;
import com.cn.xiguaapp.thrid.auth.config.AuthSource;
import com.cn.xiguaapp.thrid.auth.model.AuthUser;
import org.junit.Assert;
import org.junit.Test;

public class AuthUserTest {

    @Test
    public void serialize() {

        AuthUser user = AuthUser.builder()
            .nickname("test")
            .build();
        String json = JSON.toJSONString(user);
        Assert.assertEquals(json, "{\"nickname\":\"test\"}");

    }

    @Test
    public void deserialize() {
        AuthUser user = AuthUser.builder()
            .nickname("test")
            .build();
        String json = JSON.toJSONString(user);

        AuthUser deserializeUser = JSON.parseObject(json, AuthUser.class);
        Assert.assertEquals(deserializeUser.getNickname(), "test");
    }

    @Test
    public void source() {
        AuthSource source = AuthDefaultSource.HUAWEI;
        AuthUser user = AuthUser.builder()
            .source(source.toString())
            .build();
        Assert.assertEquals(user.getSource(), "HUAWEI");

        source = AuthExtendSource.OTHER;
        user = AuthUser.builder()
            .source(source.toString())
            .build();
        Assert.assertEquals(user.getSource(), "OTHER");

        source = AuthDefaultSource.HUAWEI;
        Assert.assertEquals(source, AuthDefaultSource.HUAWEI);

        source = AuthExtendSource.OTHER;
        Assert.assertEquals(source, AuthExtendSource.OTHER);
    }

}
