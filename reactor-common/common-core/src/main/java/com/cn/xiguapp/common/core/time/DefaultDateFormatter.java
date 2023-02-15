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

import java.util.Date;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author xiguaapp
 * @desc 默认时间处理类
 */
public class DefaultDateFormatter implements DateFormatter {
    private DateTimeFormatter formatter;
    private Predicate<String> predicate;
    private String formatterString;

    public DefaultDateFormatter(Pattern formatPattern, String formatter) {
        this((str) -> formatPattern.matcher(str).matches(), DateTimeFormat.forPattern(formatter));
        this.formatterString = formatter;
    }

    public DefaultDateFormatter(Predicate<String> predicate, DateTimeFormatter formatter) {
        this.predicate = predicate;
        this.formatter = formatter;
    }

    @Override
    public boolean support(String str) {
        return this.predicate.test(str);
    }

    @Override
    public Date format(String str) {
        return this.formatter.parseDateTime(str).toDate();
    }

    @Override
    public String getPattern() {
        return this.formatterString;
    }

    @Override
    public String toString(Date date) {
        return (new DateTime(date)).toString(this.getPattern());
    }
}
