/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/10 下午8:11 >
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

package com.cn.xiguaapp.system.api.isv.entity.param;

/**
 * @author xiguaapp
 * @Date 2020/10/10
 * @desc
 */
public interface Isv {
    /**
     * appKey
     * @return 返回appKey
     */
    String getAppKey();

    /**
     * 秘钥
     * @return 返回秘钥
     */
    String getSecretInfo();

    /**
     * 获取平台的私钥
     * @return 返回私钥
     */
    String getPrivateKeyPlatform();

    /**
     * 0启用，1禁用
     * @return 返回状态
     */
    Integer getStatus();
}
