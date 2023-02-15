/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午11:14 >
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

package com.cn.xiguaapp.common.gateway.param;

import com.cn.xiguaapp.common.gateway.bean.ApiParam;

/**
 * @author xiguaapp
 */
public interface GrayUserBuilder {

    /**
     * 获取灰度用户key
     *
     * @param apiParam apiParam
     * @return 返回用户key
     */
    String buildGrayUserKey(ApiParam apiParam);

    /**
     * 优先级，数字小优先
     *
     * @return 返回数字
     */
    int order();
}
