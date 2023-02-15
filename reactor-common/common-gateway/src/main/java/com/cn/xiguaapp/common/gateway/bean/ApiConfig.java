package com.cn.xiguaapp.common.gateway.bean;

import com.cn.xiguaapp.common.gateway.interceptor.RouteInterceptor;
import com.cn.xiguaapp.common.gateway.loadbalance.AppIdGrayUserBuilder;
import com.cn.xiguaapp.common.gateway.manage.*;
import com.cn.xiguaapp.common.gateway.param.GrayUserBuilder;
import com.cn.xiguaapp.common.gateway.param.ParameterFormatter;
import com.cn.xiguaapp.common.gateway.result.*;
import com.cn.xiguaapp.common.gateway.validate.*;
import com.cn.xiguaapp.system.api.isv.entity.param.IsvPut;
import com.cn.xiguaapp.system.api.isv.service.IIsvClient;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author xiguaapp
 * @Date 2020/10/12
 * @desc
 */
@Data
public class ApiConfig {
    private static ApiConfig instance = new ApiConfig();

    private ApiConfig() {
        grayUserBuilders = new ArrayList<>(4);
        grayUserBuilders.add(new AppIdGrayUserBuilder());
//        grayUserBuilders.add(new IpGrayUserBuilder());
    }

    /**
     * 路由配置管理器
     */
    private RouteConfigManager routeConfigManager = new DefaultRouteConfigManager();

    /**
     * gateway合并结果处理
     */
    private ResultExecutorForGateway gatewayResultExecutor = new GatewayResultExecutor();

    /**
     * isv管理
     */
    private IIsvClient isvManager;

    /**
     * 加密工具
     */
    private Encrypter encrypter = new ApiEncrypter();

    /**
     * 签名工具
     */
    private Signer signer = new ApiSigner();

    /**
     * 验证
     */
    private Validator validator = new ApiValidator();

    /**
     * isv路由权限
     */
//    private IsvRoutePermissionManager isvRoutePermissionManager = new DefaultIsvRoutePermissionManager();

    /**
     * 路由配置管理
     */
//    private RouteConfigManager routeConfigManager = new DefaultRouteConfigManager();

    /**
     * 限流配置
     */
//    private LimitConfigManager limitConfigManager = new DefaultLimitConfigManager();

    /**
     * IP黑名单
     */
//    private IPBlacklistManager ipBlacklistManager = new DefaultIPBlacklistManager();

    /**
     * 限流管理
     */
//    private LimitManager limitManager = new DefaultLimitManager();

    /**
     * 用户key管理
     */
    private EnvGrayManager userKeyManager = new DefaultEnvGrayManager();

    /**
     * 构建数据节点名称
     */
    private DataNameBuilder dataNameBuilder = new DefaultDataNameBuilder();

    /**
     * 追加结果
     */
    private ResultAppender resultAppender;

    /**
     * 处理错误信息
     */
    private ServiceErrorManager serviceErrorManager = new DefaultServiceErrorManager();

    private ParameterFormatter parameterFormatter;

    /**
     * 校验token
     */
//    private TokenValidator tokenValidator = apiParam -> apiParam != null && StringUtils.isNotBlank(apiParam.fetchAccessToken());

    /**
     * 路由拦截器
     */
    private List<RouteInterceptor> routeInterceptors = new ArrayList<>(4);

    /**
     * 监控管理
     */
//    private MonitorManager monitorManager = new MonitorManager();

    // -------- fields ---------

    /**
     * 错误模块
     */
    private List<String> i18nModules = new ArrayList<>();


    /**
     * 忽略验证，设置true，则所有接口不会进行签名校验
     */
    private boolean ignoreValidate;

    /**
     * 是否对结果进行合并。<br>
     * 默认情况下是否合并结果由微服务端决定，一旦指定该值，则由该值决定，不管微服务端如何设置。
     */
    private Boolean mergeResult;

    /**
     * 超时时间
     */
    private int timeoutSeconds = 60 * 5;

    /**
     * 是否开启限流功能
     */
    private boolean openLimit = true;

    /**
     * 显示返回sign
     */
    private boolean showReturnSign = true;

    /**
     * 保存错误信息容器的容量
     */
    private int storeErrorCapacity = 20;

    private boolean useGateway;

    private List<GrayUserBuilder> grayUserBuilders;
    public void addGrayUserBuilder(GrayUserBuilder grayUserBuilder) {
        grayUserBuilders.add(grayUserBuilder);
        grayUserBuilders.sort(Comparator.comparing(GrayUserBuilder::order));
    }

    public void addAppSecret(Map<String, String> appSecretPair) {
        for (Map.Entry<String, String> entry : appSecretPair.entrySet()) {
            this.isvManager.update(IsvPut.builder().appKey(entry.getKey())
                    .secret(entry.getValue()).build());
        }
    }

    public static ApiConfig getInstance() {
        return instance;
    }

    public static void setInstance(ApiConfig apiConfig) {
        instance = apiConfig;
    }

}
