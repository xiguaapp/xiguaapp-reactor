package com.cn.xiguaapp.common.gateway.message;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 负责构建错误消息
 *
 * @author xiguaapp
 */
@Slf4j
public class ErrorFactory {

    private ErrorFactory(){}

    public static final String SYS_ERR = "系统错误";

    private static final String I18N_OPEN_ERROR = "i18n/open/error";
    public static final String UNDERLINE = "_";

    private static Set<String> noModuleCache = new HashSet<>();

    private static Map<String, Error> errorCache = new HashMap<>(64);

    /**
     * 错误信息的国际化信息
     */
    private static MessageSourceAccessor errorMessageSourceAccessor;

    /**
     * 设置国际化资源信息
     */
    public static void initMessageSource(List<String> isvModules) {
        HashSet<String> baseNamesSet = new HashSet<>();
        baseNamesSet.add(I18N_OPEN_ERROR);

        if (!isvModules.isEmpty()) {
            baseNamesSet.addAll(isvModules);
        }

        String[] totalBaseNames = baseNamesSet.toArray(new String[0]);

        log.info("加载错误码国际化资源：{}", StringUtils.arrayToCommaDelimitedString(totalBaseNames));
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setBasenames(totalBaseNames);
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(bundleMessageSource);
        setErrorMessageSourceAccessor(messageSourceAccessor);

    }

    /**
     * 通过ErrorMeta，Locale，params构建国际化错误消息
     *
     * @param errorMeta 错误信息
     * @param locale    本地化
     * @param params    参数
     * @return 如果没有配置国际化消息，则直接返回errorMeta中的信息
     */
    public static Error getError(ErrorMeta errorMeta, Locale locale, Object... params) {
        if (locale == null) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        String key = errorMeta.getModulePrefix() + errorMeta.getCode() + errorMeta.getSubCode() + locale.toString();
        Error error = errorCache.get(key);
        if (error == null) {
            Assert.notNull(locale, "未设置Locale");
            String modulePrefix = errorMeta.getModulePrefix();
            String code = errorMeta.getCode();
            // open.error_20000=Service is temporarily unavailable
            String msg = getErrorMessage(modulePrefix + code, locale);
            String subCode = errorMeta.getSubCode();
            // open.error_20000_isp.unknown-error=Service is temporarily unavailable
            String subMsg = getErrorMessage(modulePrefix + code + UNDERLINE + subCode, locale, params);
            if (StringUtils.isEmpty(msg)) {
                msg = SYS_ERR;
            }
            if (StringUtils.isEmpty(subMsg)) {
                subMsg = SYS_ERR;
            }
            // solution暂未实现，如果要实现，可以这样配置：
            // open.error_20000_isp.unknown-error_solution=Service is temporarily unavailable
            // <code>String solution = getErrorMessage(modulePrefix + code + UNDERLINE + subCode + "_solution", locale, params);</code>
            error = new ErrorImpl(code, msg, subCode, subMsg, null);
            errorCache.put(key, error);
        }
        return error;
    }


    public static void setErrorMessageSourceAccessor(MessageSourceAccessor errorMessageSourceAccessor) {
        ErrorFactory.errorMessageSourceAccessor = errorMessageSourceAccessor;
    }

    /**
     * 返回本地化信息
     *
     * @param module 错误模块
     * @param locale 本地化
     * @param params 参数
     * @return 返回信息
     */
    public static String getErrorMessage(String module, Locale locale, Object... params) {
        if (noModuleCache.contains(module)) {
            return null;
        }
        try {
            String message = errorMessageSourceAccessor.getMessage(module, params, locale);
            return message;
        } catch (Exception e) {
            noModuleCache.add(module);
            return null;
        }
    }


}
