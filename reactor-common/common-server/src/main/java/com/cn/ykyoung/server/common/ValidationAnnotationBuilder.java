/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午5:55 >
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

package com.cn.ykyoung.server.common;

import com.cn.ykyoung.server.common.param.ValidationAnnotationDefinition;

import java.lang.annotation.Annotation;

/**
 * @author xiguaapp
 */
public interface ValidationAnnotationBuilder<T extends Annotation> {
    /**
     * 构建验证注解
     *
     * @param jsr303Annotation JSR-303注解
     * @return 返回注解定义
     */
    ValidationAnnotationDefinition build(T jsr303Annotation);
}
