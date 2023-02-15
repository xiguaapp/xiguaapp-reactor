/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:07 >
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

/**
 * @author xiguaapp
 */
public interface SignEncipher {
    /**
     * 签名的摘要算法
     * @param input 待签名数据
     * @param secret 秘钥
     * @return 返回加密后的数据
     */
    byte[] encrypt(String input, String secret);
}