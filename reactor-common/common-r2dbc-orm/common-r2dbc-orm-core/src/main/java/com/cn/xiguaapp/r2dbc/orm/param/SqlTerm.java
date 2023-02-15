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

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * 直接拼接sql的方式
 *
 * @author xiguaapp
 * @since 3.0
 */
@Getter
@Setter
public class SqlTerm extends Term {

    private String sql;

    private SqlTerm() {
    }

    public static SqlTerm of(String sql, Object... parameters) {
        return new SqlTerm(sql, parameters);
    }

    public SqlTerm(String sql, Object... value) {
        this.sql = sql;
        setValue(value);
    }

    @Override
    @SneakyThrows
    public SqlTerm clone() {
        SqlTerm term = (SqlTerm) super.clone();
        term.setSql(getSql());
        return term;
    }
}
