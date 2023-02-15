package com.cn.ykyoung.server.message;


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
 */
@Slf4j
public class ServiceErrorFactory {

    private ServiceErrorFactory(){}

    public static final String SYS_ERR = "系统错误";

    private static final String I18N_OPEN_ERROR = "i18n/isp/error";

    private static Set<String> noModuleCache = new HashSet<>();

    private static Map<String, ServiceError> errorCache = new HashMap<>(64);

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
    public static ServiceError getError(ServiceErrorMeta errorMeta, Locale locale, Object... params) {
        String key = errorMeta.getModulePrefix()  + errorMeta.getSubCode() + locale.toString();
        ServiceError error = errorCache.get(key);
        if (error == null) {
            Assert.notNull(locale, "未设置Locale");
            String modulePrefix = errorMeta.getModulePrefix();
            String subCode = errorMeta.getSubCode();
            // isp.error_sp.unknow-error=Service is temporarily unavailable
            String subMsg = getErrorMessage(modulePrefix + subCode, locale, params);
            if (StringUtils.isEmpty(subMsg)) {
                subMsg = SYS_ERR;
            }
            error = new ServiceErrorImpl(subCode, subMsg);
            errorCache.put(key, error);
        }
        return error;
    }


    public static void setErrorMessageSourceAccessor(MessageSourceAccessor errorMessageSourceAccessor) {
        ServiceErrorFactory.errorMessageSourceAccessor = errorMessageSourceAccessor;
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
            return errorMessageSourceAccessor.getMessage(module, params, locale);
        } catch (Exception e) {
            noModuleCache.add(module);
            return null;
        }
    }


}
