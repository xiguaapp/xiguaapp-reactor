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

import com.cn.xiguapp.common.core.utils.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author xiguaapp
 * @desc 时间工具类
 */
public final class DateTimeUtils {
    public static final String YEAR = "yyyy";
    public static final String YEAR_MONTH = "yyyy-MM";
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DAY_SIMPLE = "yyyyMMdd";
    public static final String YEAR_MONTH_DAY_HOUR = "yyyy-MM-dd HH";
    public static final String YEAR_MONTH_DAY_HOUR_CN = "yyyy年MM月dd日HH时";
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_SIMPLE = "yyyyMMddHHmmss";
    public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";
    public static final String HOUR_MINUTE = "HH:mm";
    public static final String MONTH_DAY = "M.d";
    private static final int DAY_SECOND = 86400;
    private static final int HOUR_SECOND = 3600;
    private static final int MINUTE_SECOND = 60;
    public static final String REG_EXP_DATE = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    public DateTimeUtils() {
    }

    public static String format(Date date, String pattern) {
        return date == null ? "" : (new DateTime(date)).toString(pattern);
    }

    public static Date formatDateString(String dateString, String pattern) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
            return dateTimeFormatter.parseDateTime(dateString).toDate();
        } catch (Exception var3) {
            return null;
        }
    }

    public static String getDayHourMinuteSecond(int second) {
        if (second == 0) {
            return "0秒";
        } else {
            StringBuilder sb = new StringBuilder();
            int days = second / 86400;
            if (days > 0) {
                sb.append(days);
                sb.append("天");
                second -= days * 86400;
            }

            int hours = second / 3600;
            if (hours > 0) {
                sb.append(hours);
                sb.append("小时");
                second -= hours * 3600;
            }

            int minutes = second / 60;
            if (minutes > 0) {
                sb.append(minutes);
                sb.append("分钟");
                second -= minutes * 60;
            }

            if (second > 0) {
                sb.append(second);
                sb.append("秒");
            }

            return sb.toString();
        }
    }

    public static String getDayHourMinute(int second) {
        if (second == 0) {
            return "0分钟";
        } else {
            StringBuilder sb = new StringBuilder();
            int days = second / 86400;
            if (days > 0) {
                sb.append(days);
                sb.append("天");
                second -= days * 86400;
            }

            int hours = second / 3600;
            if (hours > 0) {
                sb.append(hours);
                sb.append("小时");
                second -= hours * 3600;
            }

            int minutes = second / 60;
            if (minutes > 0) {
                sb.append(minutes);
                sb.append("分钟");
            }

            return sb.toString();
        }
    }

    public static DateTime getDateOnly(DateTime dateTime) {
        return new DateTime(dateTime.toString("yyyy-MM-dd"));
    }

    public static Date[] getMondayAndNextMonday() {
        DateTime dateTime = getDateOnly(new DateTime());
        DateTime monday = dateTime.dayOfWeek().withMinimumValue();
        DateTime nextMonday = monday.plusDays(7);
        return new Date[]{monday.toDate(), nextMonday.toDate()};
    }

    public static Date[] getMondayAndSunday(DateTime dateTime) {
        dateTime = getDateOnly(dateTime);
        DateTime monday = dateTime.dayOfWeek().withMinimumValue();
        DateTime sunday = monday.plusDays(6);
        return new Date[]{monday.toDate(), sunday.toDate()};
    }

    public static int compareDaysWithNow(Date date) {
        return Days.daysBetween(new DateTime(), new DateTime(date)).getDays();
    }

    public static int compareDaysWithToday(Date date) {
        DateTime today = new DateTime();
        today = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 0, 0, 0, 0);
        DateTime compareDay = new DateTime(date);
        compareDay = new DateTime(compareDay.getYear(), compareDay.getMonthOfYear(), compareDay.getDayOfMonth(), 0, 0, 0, 0);
        return Days.daysBetween(today, compareDay).getDays();
    }

    public static int compareDaysWithDay(Date a, Date b) {
        DateTime today = new DateTime(b);
        today = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 0, 0, 0, 0);
        DateTime compareDay = new DateTime(a);
        compareDay = new DateTime(compareDay.getYear(), compareDay.getMonthOfYear(), compareDay.getDayOfMonth(), 0, 0, 0, 0);
        return Days.daysBetween(today, compareDay).getDays();
    }

    public static boolean compareDateIgnoreMillisecond(Date date, Date compareDate) {
        if (date == null && compareDate == null) {
            return true;
        } else if (date == null && compareDate != null) {
            return false;
        } else if (date != null && compareDate == null) {
            return false;
        } else {
            return date.getTime() / 1000L == compareDate.getTime() / 1000L;
        }
    }

    public static int getDay(int second) {
        return second / 86400;
    }

    public static String getCompareWithTodayDateString(Date date) {
        int days = Math.abs(compareDaysWithToday(date));
        String dateString = "";
        if (days == 0) {
            dateString = "今天";
        } else if (days == 1) {
            dateString = "昨天";
        } else if (days == 2) {
            dateString = "2天前";
        } else if (days == 3) {
            dateString = "3天前";
        } else if (days == 4) {
            dateString = "4天前";
        } else if (days == 5) {
            dateString = "5天前";
        } else if (days == 6) {
            dateString = "6天前";
        } else if (days > 6 && days <= 14) {
            dateString = "1周前";
        } else if (days > 14 && days <= 21) {
            dateString = "2周前";
        } else if (days > 21 && days <= 30) {
            dateString = "3周前";
        } else if (days > 30) {
            dateString = "1月前";
        } else if (days > 365) {
            dateString = "1年前";
        } else if (days > 1095) {
            dateString = "3年前";
        }

        return dateString;
    }

    public static int compareMinutes(Date now, Date compareDate) {
        return (int)(now.getTime() - compareDate.getTime()) / '\uea60';
    }

    public static int getDayOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfMonth();
    }

    public static int getDateOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfMonth().getMaximumValue();
    }

    public static int compareYear(Date date) {
        DateTime btd = new DateTime(date);
        DateTime nowDate = new DateTime();
        int year = 0;
        if (nowDate.getMonthOfYear() > btd.getMonthOfYear()) {
            year = nowDate.getYear() - btd.getYear();
        } else if (nowDate.getMonthOfYear() < btd.getMonthOfYear()) {
            year = nowDate.getYear() - btd.getYear() - 1;
        } else if (nowDate.getMonthOfYear() == btd.getMonthOfYear()) {
            if (nowDate.getDayOfMonth() >= btd.getDayOfMonth()) {
                year = nowDate.getYear() - btd.getYear();
            } else {
                year = nowDate.getYear() - btd.getYear() - 1;
            }
        }

        return year;
    }

    public static String compareDaysWithDate(Date date, Date date2) {
        StringBuilder msg = new StringBuilder();
        int minutes = (int)Math.abs((date.getTime() - date2.getTime()) / 60000L);
        if (minutes / 60 > 0 && minutes / 60 / 24 <= 0) {
            msg.append(minutes / 60).append("小时");
        }

        if (minutes / 60 / 24 > 0) {
            msg.append(minutes / 60 / 24).append("天");
            msg.append(minutes / 60 % 24).append("小时");
        }

        return msg.toString();
    }

    public static Date formatUnknownString2Date(String dateString) {
        try {
            if (StringUtils.isNullOrEmpty(dateString)) {
                return null;
            } else {
                dateString = dateString.replace("T", " ");
                String hms = "00:00:00";
                dateString = dateString.trim();
                if (dateString.contains(" ")) {
                    hms = dateString.substring(dateString.indexOf(" ") + 1, dateString.length());
                    dateString = dateString.substring(0, dateString.indexOf(" "));
                    hms = hms.replace("：", ":");
                    hms = hms.replace("时", ":");
                    hms = hms.replace("分", ":");
                    hms = hms.replace("秒", ":");
                    hms = hms.replace("-", ":");
                    hms = hms.replace("－", ":");
                    if (hms.endsWith(":")) {
                        hms = hms.substring(0, hms.length() - 1);
                    }

                    if (hms.split(":").length == 1) {
                        hms = hms + ":00:00";
                    }

                    if (hms.split(":").length == 2) {
                        hms = hms + ":00";
                    }
                }

                String[] hmsarr = hms.split(":");
                dateString = dateString.replace(".", "-");
                dateString = dateString.replace("/", "-");
                dateString = dateString.replace("－", "-");
                dateString = dateString.replace("年", "-");
                dateString = dateString.replace("月", "-");
                dateString = dateString.replace("日", "");
                String[] ymd = dateString.split("-");
                String yearStr = ymd[0];
                String monthStr = ymd.length > 1 ? ymd[1] : "";
                String dateStr = ymd.length > 2 ? ymd[2] : "";
                monthStr = "".equals(monthStr) ? Integer.toString(1) : monthStr;
                dateStr = "".equals(dateStr) ? Integer.toString(1) : dateStr;
                String dtr = yearStr + "-" + monthStr + "-" + dateStr + " " + hms;
                return !dtr.matches("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$") ? null : (new DateTime(Integer.parseInt(yearStr.trim()), Integer.parseInt(monthStr.trim()), Integer.parseInt(dateStr.trim()), Integer.parseInt(hmsarr[0].trim()), Integer.parseInt(hmsarr[1].trim()), Integer.parseInt(hmsarr[2].trim()), 0)).toDate();
            }
        } catch (Exception var8) {
            return null;
        }
    }

    public static Date[] formatDatesByString(String dateString, String spaceChar, String splitChar) {
        if (spaceChar.equals(splitChar)) {
            return null;
        } else {
            String[] dateStrs = dateString.split(splitChar);
            Date[] dates = new Date[dateStrs.length];
            int i = 0;

            for(int size = dateStrs.length; i < size; ++i) {
                dates[i] = formatUnknownString2Date(dateStrs[i]);
            }

            return dates;
        }
    }

    public static Date identityCard2Date(String identityCard) {
        try {
            String dateStr;
            if (identityCard.length() == 18) {
                dateStr = identityCard.substring(6, 14);
                return formatDateString(dateStr, "yyyyMMdd");
            } else if (identityCard.length() == 15) {
                dateStr = identityCard.substring(6, 12);
                return formatDateString(dateStr, "yyMMdd");
            } else {
                return null;
            }
        } catch (Exception var2) {
            return null;
        }
    }

    public static boolean validDate(String str) {
        try {
            Date date = formatUnknownString2Date(str);
            return date != null;
        } catch (Exception var2) {
            return false;
        }
    }
    /**
     * 剩余分钟数
     *
     * @param date 结束点日期
     * @return 分钟数
     */
    public static final long minutesRemaining(Date date) {
        return (date.getTime() / 1000 / 60 - System.currentTimeMillis() / 1000 / 60);
    }

    /**
     * 剩余小时
     *
     * @param date 结束点日期
     * @return 小时数
     */
    public static final long remainingHours(Date date) {
        return minutesRemaining(date) / 60;
    }

    /**
     * 剩余天数
     *
     * @param date 结束点日期
     * @return 天数
     */
    public static final long remainingDays(Date date) {
        return remainingHours(date) / 24;
    }

}
