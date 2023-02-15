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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 签名验证实现
 *
 * @author xiguaapp
 */
public class ApiSigner extends AbstractSigner {

    private Map<String, SignEncipher> signEncipherMap = new HashMap<>();

    public ApiSigner() {
        signEncipherMap.put("md5", new SignEncipherMD5());
        signEncipherMap.put("hmac", new SignEncipherHMAC_MD5());
    }


    @Override
    public String buildServerSign(ApiParam param, String secret) {
        String signMethod = param.fetchSignMethod();
        SignEncipher signEncipher = signEncipherMap.get(signMethod);
        if (signEncipher == null) {
            throw ErrorEnum.ISV_INVALID_SIGNATURE_TYPE.getErrorMeta().getException(signMethod);
        }

        // 第一步：参数排序
        Set<String> keySet = param.keySet();
        List<String> paramNames = new ArrayList<>(keySet);
        Collections.sort(paramNames);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder paramNameValue = new StringBuilder();
        for (String paramName : paramNames) {
            paramNameValue.append(paramName).append(param.get(paramName));
        }

        // 第三步：使用MD5/HMAC加密
        String source = paramNameValue.toString();
        byte[] bytes = signEncipher.encrypt(source, secret);

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes).toUpperCase();
    }
}
