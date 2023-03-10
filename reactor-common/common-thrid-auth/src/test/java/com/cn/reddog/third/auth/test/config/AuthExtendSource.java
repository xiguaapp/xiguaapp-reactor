package com.cn.xiguaapp.third.auth.test.config;

import com.cn.xiguaapp.thrid.auth.config.AuthSource;

/**
 * 测试自定义实现{@link AuthSource}接口后的枚举类
 *
 * @author xiguaapp
 * @version 1.0
 * @since 1.12.0
 */
public enum AuthExtendSource implements AuthSource {

    OTHER {
        /**
         * 授权的api
         *
         * @return url
         */
        @Override
        public String authorize() {
            return "http://authorize";
        }

        /**
         * 获取accessToken的api
         *
         * @return url
         */
        @Override
        public String accessToken() {
            return "http://accessToken";
        }

        /**
         * 获取用户信息的api
         *
         * @return url
         */
        @Override
        public String userInfo() {
            return null;
        }

        /**
         * 取消授权的api
         *
         * @return url
         */
        @Override
        public String revoke() {
            return null;
        }

        /**
         * 刷新授权的api
         *
         * @return url
         */
        @Override
        public String refresh() {
            return null;
        }
    }
}
