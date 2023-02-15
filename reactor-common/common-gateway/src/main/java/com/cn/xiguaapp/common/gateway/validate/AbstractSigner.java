/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:06 >
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

package com.cn.xiguaapp.common.gateway.validate;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;
import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiguaapp
 */
@Slf4j
public abstract class AbstractSigner implements Signer {

    /**
     * 构建服务端签名串
     *
     * @param params 接口参数
     * @param secret 秘钥
     * @return 返回服务端签名串
     */
    protected abstract String buildServerSign(ApiParam params, String secret);

    @Override
    public boolean checkSign(ApiParam apiParam, String secret) {
        String clientSign = apiParam.fetchSignAndRemove();
        if (StringUtils.isBlank(clientSign)) {
            throw ErrorEnum.ISV_MISSING_SIGNATURE.getErrorMeta().getException();
        }
        String serverSign = buildServerSign(apiParam, secret);
        return clientSign.equals(serverSign);
    }

    protected static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }
}
