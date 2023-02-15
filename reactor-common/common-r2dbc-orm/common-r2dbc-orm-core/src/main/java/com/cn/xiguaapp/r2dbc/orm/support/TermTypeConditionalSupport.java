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

package com.cn.xiguaapp.r2dbc.orm.support;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 17:46
 */
public interface TermTypeConditionalSupport {
    /**
     * 条件接收器,用于处理接受到的条件并进行对应的操作如{@link Term.Type#and}
     *
     * @param <T>
     */
    interface Accepter<T,O> {
        T accept(String column, String termType, O value);
    }

    interface SimpleAccepter<T,O> {
        T accept(String column, O value);
    }
}
