package com.cn.xiguaapp.r2dbc.orm.rdb.utils;

import com.cn.xiguapp.common.core.time.DateFormatter;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.NullValue;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.PrepareSqlRequest;
import com.cn.xiguaapp.r2dbc.orm.rdb.executor.SqlRequest;
import org.slf4j.Logger;

import java.util.Date;

/**
 * @author xiguaapp
 * @desc sql工具类
 * @since 1.0.0
 */
public class SqlUtils {

    public static String sqlParameterToString(Object[] parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Object param : parameters) {
            if (i++ != 0) {
                builder.append(",");
            }
            builder.append(param);
            if (!(param instanceof NullValue)) {
                builder.append("(");
                builder.append(param == null ? "null" : param.getClass().getSimpleName());
                builder.append(")");
            }
        }
        return builder.toString();
    }

    /**
     * sql语句打印
     * @param log 日志
     * @param sqlRequest sql请求
     */
    public static void printSql(Logger log, SqlRequest sqlRequest) {
        //debug模式支持
        if (log.isDebugEnabled()) {
            //判断sql是否为空
            if (sqlRequest.isNotEmpty()) {
                boolean hasParameter = sqlRequest.getParameters() != null && sqlRequest.getParameters().length > 0;

                log.debug("==>  {}: {}", hasParameter ? "Preparing" : "  Execute", sqlRequest.getSql());
                if (hasParameter) {
                    log.debug("==> Parameters: {}", sqlParameterToString(sqlRequest.getParameters()));
                    if (sqlRequest instanceof PrepareSqlRequest) {
                        log.debug("==>     Native: {}", ((PrepareSqlRequest) sqlRequest).toNativeSql());
                    }
                }
            }
        }
    }

    /**
     *
     * @param sql sql语句
     * @param parameters 参数
     * @return
     */
    public static String toNativeSql(String sql, Object... parameters) {
        //参数为空直接返回sql语句
        if(parameters==null){
            return sql;
        }

        String[] stringParameter = new String[parameters.length];
        int len = 0;
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            //参数是否为数字
            if (parameter instanceof Number) {
                stringParameter[i] = parameter.toString();
                //参数是否为时间格式
            } else if (parameter instanceof Date) {
                stringParameter[i] = "'" + DateFormatter.toString(((Date) parameter), "yyyy-MM-dd HH:mm:ss") + "'";
                //参数是否为空
            } else if (parameter instanceof NullValue) {
                stringParameter[i] = "null";
            } else if (parameter == null) {
                stringParameter[i] = "null";
            } else {
                stringParameter[i] = "'" + parameter + "'";
            }
            len += stringParameter.length;
        }
        StringBuilder builder = new StringBuilder(sql.length() + len + 16);

        int parameterIndex = 0;
        for (int i = 0, sqlLen = sql.length(); i < sqlLen; i++) {
            char c = sql.charAt(i);
            if (c == '?') {
                if (stringParameter.length >= parameterIndex) {
                    builder.append(stringParameter[parameterIndex++]);
                } else {
                    builder.append("null");
                }
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }
}
