/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.r2dbc.orm.param;

/**
 * 提供默认支持的查询条件类型,用于动态指定查询条件
 *
 * @author xiguaapp
 * @since 1.0
 */
public interface TermType {
    /**
     * ==
     *
     * @since 1.0
     */
    String eq      = "eq";
    /**
     * !=
     *
     * @since 1.0
     */
    String not     = "not";
    /**
     * like
     *
     * @since 1.0
     */
    String like    = "like";
    /**
     * not like
     *
     * @since 1.0
     */
    String nlike   = "nlike";
    /**
     * >
     *
     * @since 1.0
     */
    String gt      = "gt";
    /**
     * <
     *
     * @since 1.0
     */
    String lt      = "lt";
    /**
     * >=
     *
     * @since 1.0
     */
    String gte     = "gte";
    /**
     * <=
     *
     * @since 1.0
     */
    String lte     = "lte";
    /**
     * in
     *
     * @since 1.0
     */
    String in      = "in";
    /**
     * notin
     *
     * @since 1.0
     */
    String nin     = "nin";
    /**
     * =''
     *
     * @since 1.0
     */
    String empty   = "empty";
    /**
     * !=''
     *
     * @since 1.0
     */
    String nempty  = "nempty";
    /**
     * is null
     *
     * @since 1.0
     */
    String isnull  = "isnull";
    /**
     * not null
     *
     * @since 1.0
     */
    String notnull = "notnull";
    /**
     * between
     *
     * @since 1.0
     */
    String btw     = "btw";
    /**
     * not between
     *
     * @since 1.0
     */
    String nbtw    = "nbtw";

}
