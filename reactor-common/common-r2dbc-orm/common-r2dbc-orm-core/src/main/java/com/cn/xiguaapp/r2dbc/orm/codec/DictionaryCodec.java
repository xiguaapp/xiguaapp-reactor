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

package com.cn.xiguaapp.r2dbc.orm.codec;

import java.util.Collection;

/**
 * @author xiguaapp
 * @desc * 字段映射器,当一个字段持有映射器时.
 *  * <ul>
 *  * <li>
 *  * 在查询时,会追加一个名为{@link DictionaryCodec#getFieldName()}的字段为{@link DictionaryCodec#decode(Object)} 的值
 *  * </li>
 *  * <li>
 *  * 在修改或者插入时,验证器会首先通过 {@link DictionaryCodec#encode(Object)}来获取一个结果.
 *  * 如果返回null.则调用{@link DictionaryCodec#decode(Object)} ,并将值放入数据库.
 *  * 如果继续返回null,则会抛出验证器异常,提示值不再选项范围中
 *  * </li>
 *  * </ul>
 *  *
 * @since 1.0 20:00
 */
public interface DictionaryCodec {
    /**
     * 获取所有选项
     *
     * @return 选项
     */
    Collection<Object> getItems();

    /**
     * 获取转换后的字段名称
     *
     * @return 转换后的字段名称
     */
    String getFieldName();

    /**
     * 将提交的数据,转换为数据库存储的数据
     *
     * @param value 提交的数据
     * @return 数据库存储的数据
     */
    Object encode(Object value);

    /**
     * 将数据库的数据,转换为目标数据
     *
     * @param data 数据库数据
     * @return 转换结果
     */
    Object decode(Object data);
}
