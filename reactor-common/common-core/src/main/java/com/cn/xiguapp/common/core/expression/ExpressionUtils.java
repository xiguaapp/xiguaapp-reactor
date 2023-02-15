/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/11/21 下午6:40 >
 *
 *       Send: 1125698980@qq.com
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cn.xiguapp.common.core.expression;

import com.cn.xiguapp.common.core.engine.DynamicScriptEngine;
import com.cn.xiguapp.common.core.engine.DynamicScriptEngineFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author xiguaapp
 * @project-name xiguaapp-reactor
 * @Date 2020/11/2 解析表达式
 *
 * @desc
 */
@Slf4j
public class ExpressionUtils {
    //表达式提取正则 ${.+?}
    private static final Pattern PATTERN = Pattern.compile("(?<=\\$\\{)(.+?)(?=})");

    /**
     * 获取默认的表达式变量
     *
     * @return 变量集合
     */
    public static Map<String, Object> getDefaultVar() {
        return new HashMap<>();
    }

    /**
     * 获取默认的表达式变量并将制定的变量合并在一起
     *
     * @param var 要合并的变量集合
     * @return 变量集合
     */
    public static Map<String, Object> getDefaultVar(Map<String, Object> var) {
        Map<String, Object> vars = getDefaultVar();
        vars.putAll(var);
        return vars;
    }

    /**
     * 使用默认的变量解析表达式
     *
     * @param expression 表达式字符串
     * @param language   表达式语言
     * @return 解析结果
     * @throws Exception 解析错误
     * @see ExpressionUtils#analytical(String, Map, String)
     */
    public static String analytical(String expression, String language) throws Exception {
        return analytical(expression, new HashMap<>(), language);
    }

    /**
     * 解析表达式,表达式使用{@link ExpressionUtils#PATTERN}进行提取<br>
     * 如调用 analytical("http://${3+2}/test",var,"spel")<br>
     * 支持的表达式语言:
     * <ul>
     * <li>freemarker</li>
     * <li>spel</li>
     * <li>ognl</li>
     * <li>groovy</li>
     * <li>js</li>
     * </ul>
     *
     * @param expression 表达式字符串
     * @param vars       变量
     * @param language   表达式语言
     * @return 解析结果
     */
    @SneakyThrows
    public static String analytical(String expression, Map<String, Object> vars, String language) {
        if (!expression.contains("${")) {
            return expression;
        }
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine(language);
        if (engine == null) {
            return expression;
        }

        return TemplateParser.parse(expression, var -> {
            if (StringUtils.isEmpty(var)) {
                return "";
            }

            if ("spel".equalsIgnoreCase(language) && !var.contains("#")) {
                try {
                    Object fast = BeanUtilsBean2.getInstance().getPropertyUtils().getProperty(vars, var);
                    if (fast != null) {
                        return fast.toString();
                    }
                } catch (Exception ignore) {
                    //ignore
                    return "";
                }
            }
            String id = DigestUtils.md5Hex(var);
            try {
                if (!engine.compiled(id)) {
                    engine.compile(id, var);
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                return String.valueOf(engine.execute(id, vars).getIfSuccess());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return "";
            }

        });
    }

}
