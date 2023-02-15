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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cn.xiguapp.common.core.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.joda.time.DateTime;

/**
 * @author xiguaapp
 * @desc jdk时间格式化工具类
 */
public class SampleJDKDateFormatter implements DateFormatter {
    Predicate<String> predicate;
    Supplier<SimpleDateFormat> formatSupplier;

    public SampleJDKDateFormatter(Predicate<String> predicate, Supplier<SimpleDateFormat> formatSupplier) {
        this.predicate = predicate;
        this.formatSupplier = formatSupplier;
    }

    @Override
    public boolean support(String str) {
        return this.predicate.test(str);
    }

    @Override
    public Date format(String str) {
        try {
            return ((SimpleDateFormat)this.formatSupplier.get()).parse(str);
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    @Override
    public String getPattern() {
        return ((SimpleDateFormat)this.formatSupplier.get()).toPattern();
    }

    @Override
    public String toString(Date date) {
        return (new DateTime(date)).toString(this.getPattern());
    }
}
