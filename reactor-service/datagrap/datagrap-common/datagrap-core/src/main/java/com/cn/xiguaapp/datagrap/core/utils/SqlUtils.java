package com.cn.xiguaapp.datagrap.core.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author xiguaapp
 * @date 2021-02-21
 * @desc sql工具类
 */
public class SqlUtils {
    public static String toSqlString(Object obj){
        StringBuilder sql = new StringBuilder();
        if(obj instanceof Collection){
            Collection c = (Collection)obj;
            Iterator it = c.iterator();
            while(it.hasNext()){
                sql.append(",").append(it.next());
            }
        }
        if(sql.length()>0) {
            return sql.substring(1);
        }
        return null;
    }
}
