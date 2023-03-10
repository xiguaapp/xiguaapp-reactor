package com.cn.xiguaapp.datagrap.core.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc 排序工具类
 */
public class SortUtils {
    public static List<Map<String, Object>> marketSort(List<Map<String, Object>> list)
    {
        list.sort((o1, o2) -> {
            Object totalCount1 = o1.get("totalCount");
            Object totalCount2 = o2.get("totalCount");
            String count1Str = totalCount1 == null ? "0" : totalCount1.toString();
            String count2Str = totalCount2 == null ? "0" : totalCount2.toString();
            int count1 = Integer.parseInt(count1Str);
            int count2 = Integer.parseInt(count2Str);
            return Integer.compare(count2, count1);
        });
        return list;
    }

    public static List<Map<String, Object>> customerSort(List<Map<String, Object>> list)
    {
        list.sort((o1, o2) -> {
            Object totalCount1 = o1.get("appointDetectionCount");
            Object totalCount2 = o2.get("appointDetectionCount");
            String count1Str = totalCount1 == null ? "0" : totalCount1.toString();
            String count2Str = totalCount2 == null ? "0" : totalCount2.toString();
            int count1 = Integer.parseInt(count1Str);
            int count2 = Integer.parseInt(count2Str);
            return Integer.compare(count2, count1);
        });
        return list;
    }

    public static List<Map<String, Object>> caraveNewSort(List<Map<String, Object>> list)
    {
        list.sort((o1, o2) -> {
            Object totalCount1 = o1.get("today_ave");
            Object totalCount2 = o2.get("today_ave");
            String count1Str = totalCount1 == null ? "0" : totalCount1.toString();
            String count2Str = totalCount2 == null ? "0" : totalCount2.toString();
            float count1 = Float.parseFloat(count1Str);
            float count2 = Float.parseFloat(count2Str);
            return Float.compare(count2, count1);
        });
        return list;
    }

    public static List<Map<String, Object>> bidaveSort(List<Map<String, Object>> list)
    {

        list.sort((o1, o2) -> {
            Object totalCount1 = o1.get("today_ave");
            Object totalCount2 = o2.get("today_ave");
            String count1Str = totalCount1 == null ? "0" : totalCount1.toString();
            String count2Str = totalCount2 == null ? "0" : totalCount2.toString();
            float count1 = Float.parseFloat(count1Str);
            float count2 = Float.parseFloat(count2Str);
            return Float.compare(count2, count1);
        });
        return list;

    }

    public static List<Map<String, Object>> bidratioSort(List<Map<String, Object>> list)
    {
        list.sort((o1, o2) -> {
            Object totalCount1 = o1.get("today_ratio");
            Object totalCount2 = o2.get("today_ratio");
            String count1Str = totalCount1 == null ? "0" : totalCount1.toString();
            String count2Str = totalCount2 == null ? "0" : totalCount2.toString();
            float count1 = Float.parseFloat(count1Str);
            float count2 = Float.parseFloat(count2Str);
            return Float.compare(count2, count1);
        });
        return list;
    }
}
