package com.cn.xiguaapp.common.oauth.core.action;

import com.cn.xiguaapp.common.oauth.core.session.SaSession;

/**
 * @author xiguaapp
 * @desc 逻辑代理接口
 *    此接口将会代理框架内部的一些关键性逻辑，方便开发者进行按需重写
 * @since 1.0 00:54
 */
public interface SaTokenAction {
    /**
     * 根据一定的算法生成一个token
     * @param loginId 账号id
     * @param loginKey 账号体系key
     * @return 一个token
     */
    public String createToken(Object loginId, String loginKey);

    /**
     * 根据 SessionId 创建一个 Session
     * @param sessionId Session的Id
     * @return 创建后的Session
     */
    public SaSession createSession(String sessionId);
}
