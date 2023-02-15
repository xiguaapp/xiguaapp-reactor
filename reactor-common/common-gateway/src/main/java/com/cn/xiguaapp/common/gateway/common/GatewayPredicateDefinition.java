/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午4:47 >
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

package com.cn.xiguaapp.common.gateway.common;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiguaapp
 */
@Data
public class GatewayPredicateDefinition {
    public static final String GEN_KEY = "_genkey_";
    /** 断言对应的Name */
    private String name;
    /** 配置的断言规则 */
    private Map<String, String> args = new LinkedHashMap<>();

    public GatewayPredicateDefinition() {
    }

    public GatewayPredicateDefinition(String text) {
        int eqIdx = text.indexOf(61);
        if (eqIdx <= 0) {
            throw new RuntimeException("Unable to parse GatewayPredicateDefinition text '" + text + "', must be of the form name=value");
        } else {
            this.setName(text.substring(0, eqIdx));
            String[] params = StringUtils.tokenizeToStringArray(text.substring(eqIdx + 1), ",");

            for(int i = 0; i < params.length; ++i) {
                this.args.put(generateName(i), params[i]);
            }

        }
    }

    public static String generateName(int i) {
        return GEN_KEY + i;
    }
}