/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/9 上午10:26 >
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

import com.cn.xiguaapp.common.gateway.bean.ErrorDefinition;
import com.cn.xiguaapp.common.gateway.bean.ErrorEntity;

import java.util.Collection;

/**
 * @author xiguaapp
 * @desc 错误信息
 * @since 1.0 10:26
 */
public interface ServiceErrorManager {
    /**
     * 保存业务错误，一般由开发人员自己throw的异常
     * @param errorDefinition
     */
    void saveBizError(ErrorDefinition errorDefinition);

    /**
     * 保存未知的错误信息
     * @param errorDefinition
     */
    void saveUnknownError(ErrorDefinition errorDefinition);

    /**
     * 清除日志
     */
    void clear();

    /**
     * 获取所有错误信息
     * @return
     */
    Collection<ErrorEntity> listAllErrors();
}
