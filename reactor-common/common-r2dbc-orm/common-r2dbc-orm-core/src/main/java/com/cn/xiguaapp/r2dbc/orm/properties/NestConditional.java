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

package com.cn.xiguaapp.r2dbc.orm.properties;

import com.cn.xiguapp.common.core.utils.StringUtils;
import com.cn.xiguaapp.r2dbc.orm.operator.LogicalOperation;
import com.cn.xiguaapp.r2dbc.orm.param.MethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import com.cn.xiguaapp.r2dbc.orm.param.Term;
import com.cn.xiguaapp.r2dbc.orm.param.TermType;
import com.cn.xiguaapp.r2dbc.orm.support.TermTypeConditionalSupport;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface NestConditional<T extends TermTypeConditionalSupport> extends LogicalOperation<NestConditional<T>>, TermTypeConditionalSupport {

    T end();

    NestConditional<NestConditional<T>> nest();

    NestConditional<NestConditional<T>> nest(String column, Object value);

    NestConditional<NestConditional<T>> orNest();

    NestConditional<NestConditional<T>> orNest(String column, Object value);

    NestConditional<T> and();

    NestConditional<T> or();

    NestConditional<T> and(String column, String termType, Object value);

    NestConditional<T> or(String column, String termType, Object value);

    default T and(Supplier<Term> termSupplier) {
        Term term = termSupplier.get();
        term.setType(Term.Type.and);
        accept(term);
        return (T) this;
    }

    default T or(Supplier<Term> termSupplier) {
        Term term = termSupplier.get();
        term.setType(Term.Type.or);
        accept(term);
        return (T) this;
    }

    default <B> NestConditional<T> and(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return and(column.getColumn(), termType, value);
    }

    default <B> NestConditional<T> or(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return or(column.getColumn(), termType, value);
    }

    default NestConditional<T> and(String column, Object value) {
        and();
        return and(column, TermType.eq, value);
    }

    default NestConditional<T> or(String column, Object value) {
        or();
        return or(column, TermType.eq, value);
    }

    default NestConditional<T> like(String column, Object value) {
        return accept(column, TermType.like, value);
    }

    default NestConditional<T> like$(String column, Object value) {
        if (value == null)
            return like(column, null);
        return accept(column, TermType.like, String.valueOf(value).concat("%"));
    }

    default NestConditional<T> $like(String column, Object value) {
        if (value == null)
            return like(column, null);
        return accept(column, TermType.like, "%".concat(String.valueOf(value)));
    }

    default NestConditional<T> $like$(String column, Object value) {
        if (value == null)
            return like(column, null);
        return accept(column, TermType.like, "%".concat(String.valueOf(value)).concat("%"));
    }

    default NestConditional<T> is(String column, Object value) {
        return accept(column, TermType.eq, value);
    }

    default NestConditional<T> notLike(String column, Object value) {
        return accept(column, TermType.nlike, value);
    }

    default NestConditional<T> gt(String column, Object value) {
        return accept(column, TermType.gt, value);
    }

    default NestConditional<T> lt(String column, Object value) {
        return accept(column, TermType.lt, value);
    }

    default NestConditional<T> gte(String column, Object value) {
        return accept(column, TermType.gte, value);
    }

    default NestConditional<T> lte(String column, Object value) {
        return accept(column, TermType.lte, value);
    }

    default NestConditional<T> in(String column, Object... values) {
        return accept(column, TermType.in, values);
    }

    default NestConditional<T> in(String column, Object value) {
        return accept(column, TermType.in, value);
    }

    default NestConditional<T> in(String column, Collection values) {
        return accept(column, TermType.in, values);
    }

    default NestConditional<T> notIn(String column, Object value) {
        return accept(column, TermType.nin, value);
    }

    default NestConditional<T> isEmpty(String column) {
        return accept(column, TermType.empty, 1);
    }

    default NestConditional<T> notEmpty(String column) {
        return accept(column, TermType.nempty, 1);
    }

    default NestConditional<T> isNull(String column) {
        return accept(column, TermType.isnull, 1);
    }

    default NestConditional<T> notNull(String column) {
        return accept(column, TermType.notnull, 1);
    }

    default NestConditional<T> not(String column, Object value) {
        return accept(column, TermType.not, value);
    }

    default NestConditional<T> between(String column, Object between, Object and) {
        return accept(column, TermType.btw, Arrays.asList(between, and));
    }

    default NestConditional<T> notBetween(String column, Object between, Object and) {
        return accept(column, TermType.nbtw, Arrays.asList(between, and));
    }

    /*------lambda-------*/

    default <B> NestConditional<T> is(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.eq, value);
    }

    default <B> NestConditional<T> is(MethodReferenceColumn<B> column) {
        return accept(column, TermType.eq);
    }
    
    default <B> NestConditional<T> like(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.like, value);
    }

    default <B> NestConditional<T> like(MethodReferenceColumn<B> column) {
        return accept(column, TermType.like);
    }

    default <B> NestConditional<T> like$(MethodReferenceColumn<B> column) {
        Object value = column.get();
        if (value == null)
            return like(column.getColumn(), null);
        return accept(column.getColumn(), TermType.like, StringUtils.concat(value, "%"));
    }

    default <B> NestConditional<T> $like(MethodReferenceColumn<B> column) {
        Object value = column.get();
        if (value == null)
            return like(column.getColumn(), null);
        return accept(column.getColumn(), TermType.like, StringUtils.concat("%", value));
    }


    default <B> NestConditional<T> $like$(MethodReferenceColumn<B> column) {
        Object value = column.get();
        if (value == null)
            return like(column.getColumn(), null);
        return accept(column.getColumn(), TermType.like, StringUtils.concat("%", value, "%"));
    }

    default <B> NestConditional<T> like$(StaticMethodReferenceColumn<B> column, Object value) {
        if (value == null)
            return like(column, null);
        return accept(column, TermType.like, String.valueOf(value).concat("%"));
    }

    default <B> NestConditional<T> $like(StaticMethodReferenceColumn<B> column, Object value) {
        if (value == null)
            return like(column, null);
        return accept(column, TermType.like, "%".concat(String.valueOf(value)));
    }

    default <B> NestConditional<T> $like$(StaticMethodReferenceColumn<B> column, Object value) {
        if (value == null)
            return like(column, null);
        return accept(column, TermType.like, "%".concat(String.valueOf(value)).concat("%"));
    }

    default <B> NestConditional<T> notLike(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.nlike, value);
    }

    default <B> NestConditional<T> notLike(MethodReferenceColumn<B> column) {
        return accept(column, TermType.nlike);
    }

    default <B> NestConditional<T> gt(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.gt, value);
    }

    default <B> NestConditional<T> gt(MethodReferenceColumn<B> column) {
        return accept(column, TermType.gt);
    }

    default <B> NestConditional<T> lt(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.lt, value);
    }

    default <B> NestConditional<T> lt(MethodReferenceColumn<B> column) {
        return accept(column, TermType.lt);
    }

    default <B> NestConditional<T> gte(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.gte, value);
    }

    default <B> NestConditional<T> gte(MethodReferenceColumn<B> column) {
        return accept(column, TermType.gte);
    }

    default <B> NestConditional<T> lte(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.lte, value);
    }

    default <B> NestConditional<T> lte(MethodReferenceColumn<B> column) {
        return accept(column, TermType.lte);
    }

    default <B> NestConditional<T> in(StaticMethodReferenceColumn<B> column, Object... values) {
        return accept(column, TermType.in, values);
    }

    default <B> NestConditional<T> in(MethodReferenceColumn<B> column) {
        return accept(column, TermType.in);
    }

    default <B> NestConditional<T> in(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.in, value);
    }

    default <B> NestConditional<T> in(StaticMethodReferenceColumn<B> column, Collection values) {
        return accept(column, TermType.in, values);
    }

    default <B> NestConditional<T> notIn(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.nin, value);
    }

    default <B> NestConditional<T> notIn(MethodReferenceColumn<B> column) {
        return accept(column, TermType.nin);
    }

    default <B> NestConditional<T> isEmpty(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.empty, 1);
    }

    default <B> NestConditional<T> notEmpty(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.nempty, 1);
    }

    default <B> NestConditional<T> isNull(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.isnull, 1);
    }

    default <B> NestConditional<T> notNull(StaticMethodReferenceColumn<B> column) {
        return accept(column, TermType.notnull, 1);
    }

    default <B> NestConditional<T> not(StaticMethodReferenceColumn<B> column, Object value) {
        return accept(column, TermType.not, value);
    }

    default <B> NestConditional<T> not(MethodReferenceColumn<B> column) {
        return accept(column, TermType.not);
    }

    default <B> NestConditional<T> between(MethodReferenceColumn<B> column, Function<B, Object> between, Function<B, Object> and) {
        B value = column.get();
        return accept(column.getColumn(), TermType.btw, Arrays.asList(between.apply(value), and.apply(value)));
    }

    default <B> NestConditional<T> between(StaticMethodReferenceColumn<B> column, Object between, Object and) {
        return accept(column, TermType.btw, Arrays.asList(between, and));
    }

    default <B> NestConditional<T> notBetween(StaticMethodReferenceColumn<B> column, Object between, Object and) {
        return accept(column, TermType.nbtw, Arrays.asList(between, and));
    }

    default <B> NestConditional<T> accept(StaticMethodReferenceColumn<B> column, String termType, Object value) {
        return getAccepter().accept(column.getColumn(), termType, value);
    }

    default NestConditional<T> accept(String column, String termType, Object value) {
        return getAccepter().accept(column, termType, value);
    }

    default <B> NestConditional<T> accept(MethodReferenceColumn<B> column, String termType) {
        return getAccepter().accept(column.getColumn(), termType, column.get());
    }

    Accepter<NestConditional<T>, Object> getAccepter();

    NestConditional<T> accept(Term term);
}
