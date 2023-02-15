package com.cn.common.xiguaapp.test.publish01;

import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 10:26
 */
public class Test {
    public static void main(String[] args) {
        String s = LocalDate.now().toString();
        System.out.println(s.substring(0,10));
    }
}
