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

import com.cn.xiguaapp.r2dbc.orm.param.StaticMethodReferenceColumn;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface TermTypeConditionalFromBeanSupport<B> {
    Logger logger = LoggerFactory.getLogger("queryForBean");

    B getBean();

    default Object getValue(StaticMethodReferenceColumn<B> property) {
        return property.apply(getBean());
    }

    default Object getValue(String property) {
        if (getBean() == null) {
            return null;
        }
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        try {
            return propertyUtilsBean.getProperty(getBean(), property);
        } catch (Exception e) {
            logger.warn("get bean property {} error", property, e);
        }
        return null;
    }

}
