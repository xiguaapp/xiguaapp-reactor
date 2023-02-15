/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:11 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.xiguaapp.common.gateway.manage;

/**
 * @author xiguaapp
 */

public enum EnvironmentKeys {
    SPRING_PROFILES_ACTIVE("spring.profiles.active"),
    /**
     * spring.application.name
     */
    SPRING_APPLICATION_NAME("spring.application.name"),

    /**
     * sign.urlencode=true，签名验证拼接字符串的value部分进行urlencode
     */
    SIGN_URLENCODE("sign.urlencode"),

    /**
     * restful.path=/xx ，指定请求前缀，默认/rest
     */
    SOP_RESTFUL_PATH("restful.path", "/rest"),

    /**
     * 排除其它微服务，多个用英文逗号隔开
     */
    SOP_SERVICE_EXCLUDE("service.exclude"),
    /**
     * 排除其它微服务，正则形式，多个用英文逗号隔开
     */
    SOP_SERVICE_EXCLUDE_REGEX("service.exclude-regex"),
    /**
     * 预发布域名
     */
    PRE_DOMAIN("pre.domain"),

    /**
     * post请求body缓存大小
     */
    MAX_IN_MEMORY_SIZE("spring.codec.max-in-memory-size", "262144"),

    ;

    private final String key;
    private String defaultValue;

    public String getKey() {
        return key;
    }

    EnvironmentKeys(String key) {
        this.key = key;
    }

    EnvironmentKeys(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        return EnvironmentContext.getValue(key, defaultValue);
    }

    public String getValue(String defaultValue) {
        return EnvironmentContext.getValue(key, defaultValue);
    }
}