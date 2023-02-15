package com.cn.xiguaapp.common.gateway.message;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc 错误实体类
 */
public enum ErrorEnum {
    /** 成功 */
    SUCCESS(Codes.CODE_SUCCESS, ""),

    /** 服务暂不可用 */
    ISP_UNKNOWN_ERROR(Codes.CODE_UNKNOWN, "isp.unknown-error"),
    /** 服务不可用，路由被禁用 */
    ISP_API_DISABLED(Codes.CODE_UNKNOWN, "isp.service-not-available"),
    /** 限流处理 */
    ISV_REQUEST_LIMIT(Codes.CODE_UNKNOWN, "isv.service-busy"),

    /** 无效的访问令牌 */
    AOP_INVALID_AUTH_TOKEN(Codes.CODE_AUTH, "aop.invalid-auth-token"),
    /** 访问令牌已过期 */
    AOP_AUTH_TOKEN_TIME_OUT(Codes.CODE_AUTH, "aop.auth-token-time-out"),
    /** 无效的应用授权令牌 */
    AOP_INVALID_APP_AUTH_TOKEN(Codes.CODE_AUTH, "aop.invalid-app-auth-token"),
    /** 商户未授权当前接口 */
    AOP_INVALID_APP_AUTH_TOKEN_NO_API(Codes.CODE_AUTH, "aop.invalid-app-auth-token-no-api"),
    /** 应用授权令牌已过期 */
    AOP_APP_AUTH_TOKEN_TIME_OUT(Codes.CODE_AUTH, "aop.app-auth-token-time-out"),
    /** 商户未签约任何产品 */
    AOP_NO_PRODUCT_REG_BY_PARTNER(Codes.CODE_AUTH, "aop.no-product-reg-by-partner"),

    /** 缺少方法名参数 */
    ISV_MISSING_METHOD(Codes.CODE_MISSING, "isv.missing-method"),
    /** 缺少签名参数 */
    ISV_MISSING_SIGNATURE(Codes.CODE_MISSING, "isv.missing-signature"),
    /** 缺少签名类型参数 */
    ISV_MISSING_SIGNATURE_TYPE(Codes.CODE_MISSING, "isv.missing-signature-type"),
    /** 缺少签名配置 */
    ISV_MISSING_SIGNATURE_KEY(Codes.CODE_MISSING, "isv.missing-signature-key"),
    /** 缺少appId参数 */
    ISV_MISSING_APP_ID(Codes.CODE_MISSING, "isv.missing-app-id"),
    /** 缺少时间戳参数 */
    ISV_MISSING_TIMESTAMP(Codes.CODE_MISSING, "isv.missing-timestamp"),
    /** 缺少版本参数 */
    ISV_MISSING_VERSION(Codes.CODE_MISSING, "isv.missing-version"),
    /** 解密出错, 未指定加密算法 */
    ISV_DECRYPTION_ERROR_MISSING_ENCRYPT_TYPE(Codes.CODE_MISSING, "isv.decryption-error-missing-encrypt-type"),

    /** 参数无效 */
    ISV_INVALID_PARAMETER(Codes.CODE_INVALID, "isv.invalid-parameter"),
    /** 文件上传失败 */
    ISV_UPLOAD_FAIL(Codes.CODE_INVALID, "isv.upload-fail"),
    /** 文件扩展名无效 */
    ISV_INVALID_FILE_EXTENSION(Codes.CODE_INVALID, "isv.invalid-file-extension"),
    /** 文件大小无效 */
    ISV_INVALID_FILE_SIZE(Codes.CODE_INVALID, "isv.invalid-file-size"),
    /** 不存在的方法名 */
    ISV_INVALID_METHOD(Codes.CODE_INVALID, "isv.invalid-method"),
    /** 无效的数据格式 */
    ISV_INVALID_FORMAT(Codes.CODE_INVALID, "isv.invalid-format"),
    /** 无效的签名类型 */
    ISV_INVALID_SIGNATURE_TYPE(Codes.CODE_INVALID, "isv.invalid-signature-type"),
    /** 无效签名 */
    ISV_INVALID_SIGNATURE(Codes.CODE_INVALID, "isv.invalid-signature"),
    /** 无效的加密类型 */
    ISV_INVALID_ENCRYPT_TYPE(Codes.CODE_INVALID, "isv.invalid-encrypt-type"),
    /** 解密异常 */
    ISV_INVALID_ENCRYPT(Codes.CODE_INVALID, "isv.invalid-encrypt"),
    /** 无效的appId参数 */
    ISV_INVALID_APP_ID(Codes.CODE_INVALID, "isv.invalid-app-id"),
    /** 非法的时间戳参数 */
    ISV_INVALID_TIMESTAMP(Codes.CODE_INVALID, "isv.invalid-timestamp"),
    /** 字符集错误 */
    ISV_INVALID_CHARSET(Codes.CODE_INVALID, "isv.invalid-charset"),
    /** 摘要错误 */
    ISV_INVALID_DIGEST(Codes.CODE_INVALID, "isv.invalid-digest"),
    /** 解密出错，不支持的加密算法 */
    ISV_DECRYPTION_ERROR_NOT_VALID_ENCRYPT_TYPE(Codes.CODE_INVALID, "isv.decryption-error-not-valid-encrypt-type"),
    /** 解密出错, 未配置加密密钥或加密密钥格式错误 */
    ISV_DECRYPTION_ERROR_NOT_VALID_ENCRYPT_KEY(Codes.CODE_INVALID, "isv.decryption-error-not-valid-encrypt-key"),
    /** 解密出错，未知异常 */
    ISV_DECRYPTION_ERROR_UNKNOWN(Codes.CODE_INVALID, "isv.decryption-error-unknown"),
    /** 验签出错, 未配置对应签名算法的公钥或者证书 */
    ISV_MISSING_SIGNATURE_CONFIG(Codes.CODE_INVALID, "isv.missing-signature-config"),
    /** 本接口不支持第三方代理调用 */
    ISV_NOT_SUPPORT_APP_AUTH(Codes.CODE_INVALID, "isv.not-support-app-auth"),
    /** 可疑的攻击请求 */
    ISV_SUSPECTED_ATTACK(Codes.CODE_INVALID, "isv.suspected-attack"),
    /** 无效的content-type */
    ISV_INVALID_CONTENT_TYPE(Codes.CODE_INVALID, "isv.invalid-content-type"),

    /** 业务处理失败 */
    BIZ_ERROR(Codes.CODE_BIZ, ""),

    /** 请检查配置的账户是否有当前接口权限 */
    ISV_INSUFFICIENT_ISV_PERMISSIONS(Codes.CODE_ISV_PERM, "isv.insufficient-isv-permissions"),
    /** 代理的商户没有当前接口权限 */
    ISV_INSUFFICIENT_USER_PERMISSIONS(Codes.CODE_ISV_PERM, "isv.insufficient-user-permissions"),
    /** 没有当前接口权限 */
    ISV_ROUTE_NO_PERMISSIONS(Codes.CODE_ISV_PERM, "isv.route-no-permissions"),
    /** 禁止访问 */
    ISV_ACCESS_FORBIDDEN(Codes.CODE_ISV_PERM, "isv.access-forbidden"),
    /** 禁止IP访问 */
    ISV_IP_FORBIDDEN(Codes.CODE_ISV_PERM, "isv.ip-forbidden"),

    ;
    private ErrorMeta errorMeta;

    ErrorEnum(String code, String subCode) {
        this.errorMeta = new ErrorMeta("open.error_", code, subCode);
    }

    public ErrorMeta getErrorMeta() {
        return errorMeta;
    }

    private static class Codes {
        public static final String CODE_SUCCESS = "10000";
        public static final String CODE_AUTH = "20001";
        public static final String CODE_MISSING = "40001";
        public static final String CODE_INVALID = "40002";
        public static final String CODE_BIZ = "40004";
        public static final String CODE_ISV_PERM = "40006";
        public static final String CODE_UNKNOWN = "20000";
    }
}
