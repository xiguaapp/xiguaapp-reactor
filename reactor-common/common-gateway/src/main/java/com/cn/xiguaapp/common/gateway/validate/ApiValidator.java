/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午5:04 >
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

package com.cn.xiguaapp.common.gateway.validate;

import com.cn.xiguaapp.common.gateway.bean.*;
import com.cn.xiguaapp.common.gateway.manage.RouteConfigManager;
import com.cn.xiguaapp.common.gateway.manage.RouteRepositoryContext;
import com.cn.xiguaapp.common.gateway.message.ErrorEnum;
import com.cn.xiguaapp.system.api.isv.service.IIsvClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiguaapp
 * @desc 参数验证 ip校验 权限校验黑名单校验
 */
@Slf4j
@Getter
public class ApiValidator implements Validator {

    private static final int MILLISECOND_OF_ONE_SECOND = 1000;
    private static final int STATUS_FORBIDDEN = 2;

    private static final List<String> FORMAT_LIST = Arrays.asList("json", "xml");

    @Autowired
    private IIsvClient isvManager;

//    @Autowired
//    private IsvRoutePermissionManager isvRoutePermissionManager;
//
//    @Autowired
//    private IPBlacklistManager ipBlacklistManager;
//
    @Autowired
    private RouteConfigManager routeConfigManager;

    /**
     * 单个文件大小
     */
    @Value("${upload.max-file-size:${spring.servlet.multipart.max-file-size:10MB}}")
    private String maxFileSize;

//
    @Override
    public void validate(ApiParam param) {
        checkIP(param); //ip校验
        TargetRoute targetRoute = checkEnable(param);
        initFields(targetRoute, param);
        ApiConfig apiConfig = ApiContext.getApiConfig();
        checkAppKey().accept(param);
        if (apiConfig.isIgnoreValidate()
                || BooleanUtils.toBoolean(targetRoute.getRouteDefinition().getIgnoreValidate())) {
            if (log.isDebugEnabled()) {
                log.debug("忽略签名校验, name:{}, version:{}", param.fetchName(), param.fetchVersion());
            }
        } else {
            checkSign(param);
        }
        checkTimeout(param);
        checkFormat(param);
        checkUploadFile(param);
        checkPermission(param);
        checkToken(param);
    }

    /**
     * 是否在IP黑名单中
     *
     * @param param 接口参数
     */
    protected void checkIP(ApiParam param) {
        String ip = param.fetchIp();
//        if (ipBlacklistManager.contains(ip)) {
//            throw ErrorEnum.ISV_IP_FORBIDDEN.getErrorMeta().getException();
//        }
    }

    /**
     * 检测能否访问
     *
     * @param param 接口参数
     */
    protected TargetRoute checkEnable(ApiParam param) {
        String name = param.fetchName();
        if (name == null) {
            throw ErrorEnum.ISV_MISSING_METHOD.getErrorMeta().getException();
        }
        String version = param.fetchVersion();
        if (version == null) {
            throw ErrorEnum.ISV_MISSING_VERSION.getErrorMeta().getException();
        }
        String routeId = param.fetchNameVersion();
        // 检查路由是否存在
        TargetRoute targetRoute = RouteRepositoryContext.getTargetRoute(routeId);
        if (targetRoute == null) {
            throw ErrorEnum.ISV_INVALID_METHOD.getErrorMeta().getException();
        }
        // 检查路由是否启用
        RouteConfig routeConfig = routeConfigManager.get(routeId);
        if (!routeConfig.enable()) {
            throw ErrorEnum.ISP_API_DISABLED.getErrorMeta().getException();
        }
        return targetRoute;
    }

    private void initFields(TargetRoute targetRoute, ApiParam apiParam) {
        apiParam.setServiceId(targetRoute.getServiceDefinition().getServiceId());
        boolean mergeResult;
        Boolean defaultSetting = ApiContext.getApiConfig().getMergeResult();
        if (defaultSetting != null) {
            mergeResult = defaultSetting;
        } else {
            RouteDefinition routeDefinition = targetRoute.getRouteDefinition();
            mergeResult = routeDefinition == null || BooleanUtils.toBoolean(routeDefinition.getMergeResult());
        }
        apiParam.setMergeResult(mergeResult);
    }

    /**
     * 校验上传文件内容
     *
     * @param param
     */
    protected void checkUploadFile(ApiParam param) {
        UploadContext uploadContext = param.fetchUploadContext();
        if (uploadContext != null) {
            try {
                List<MultipartFile> files = uploadContext.getAllFile();
                for (MultipartFile file : files) {
                    checkSingleFileSize(file);
                    checkFileMd5(param, file);
                }
            } catch (IOException e) {
                log.error("验证上传文件MD5错误", e);
                throw ErrorEnum.ISV_UPLOAD_FAIL.getErrorMeta().getException();
            }
        }
    }

    private void checkFileMd5(ApiParam param, MultipartFile file) throws IOException {
        // 客户端传来的文件md5
        String clientMd5 = param.getString(file.getName());
        if (clientMd5 != null) {
            String fileMd5 = DigestUtils.md5Hex(file.getBytes());
            if (!clientMd5.equals(fileMd5)) {
                throw ErrorEnum.ISV_UPLOAD_FAIL.getErrorMeta().getException();
            }
        }
    }

    /**
     * 校验单个文件大小
     *
     * @param file 文件
     */
    private void checkSingleFileSize(MultipartFile file) {
        long fileSize = file.getSize();
        if (fileSize > DataSize.parse(maxFileSize).toBytes()) {
            throw ErrorEnum.ISV_INVALID_FILE_SIZE.getErrorMeta().getException(file.getName(), maxFileSize);
        }
    }

    protected void checkTimeout(ApiParam param) {
        int timeoutSeconds = ApiContext.getApiConfig().getTimeoutSeconds();
        // 如果设置为0，表示不校验
        if (timeoutSeconds == 0) {
            return;
        }
        if (timeoutSeconds < 0) {
            throw new IllegalArgumentException("服务端timeoutSeconds设置错误");
        }
        String requestTime = param.fetchTimestamp();
        try {
            Date requestDate = new SimpleDateFormat(ParamNames.TIMESTAMP_PATTERN).parse(requestTime);
            long requestMilliseconds = requestDate.getTime();
            if (System.currentTimeMillis() - requestMilliseconds > timeoutSeconds * MILLISECOND_OF_ONE_SECOND) {
                throw ErrorEnum.ISV_INVALID_TIMESTAMP.getErrorMeta().getException();
            }
        } catch (ParseException e) {
            throw ErrorEnum.ISV_INVALID_TIMESTAMP.getErrorMeta().getException(param.fetchNameVersion());
        }
    }

    protected Consumer<ApiParam> checkAppKey() {
        return param->{
            if (StringUtils.isEmpty(param.fetchAppKey())) {
                throw ErrorEnum.ISV_MISSING_APP_ID.getErrorMeta().getException();
            }
            isvManager.getIsv(param.fetchAppKey())
                    //isv用户不存在
                    .switchIfEmpty(Mono.error(()->ErrorEnum.ISV_INVALID_APP_ID.getErrorMeta().getException()))
                    .doOnNext(oo->{
                        //禁止访问或数据异常情况
                        if (oo.getStatus()==null||oo.getStatus()==STATUS_FORBIDDEN){
                            throw ErrorEnum.ISV_ACCESS_FORBIDDEN.getErrorMeta().getException();
                        }
                    }).subscribeOn(Schedulers.elastic());
        };
    }

    protected void checkSign(ApiParam param) {
        String clientSign = param.fetchSign();
        try {
            if (StringUtils.isEmpty(clientSign)) {
                throw ErrorEnum.ISV_MISSING_SIGNATURE.getErrorMeta().getException(param.fetchNameVersion(), ParamNames.SIGN_NAME);
            }
            ApiConfig apiConfig = ApiContext.getApiConfig();
            // 根据appId获取秘钥
            Isv isvInfo = (Isv) isvManager.getIsv(param.fetchAppKey()).block();
            String secret = isvInfo.getSecretInfo();
            if (StringUtils.isEmpty(secret)) {
                throw ErrorEnum.ISV_MISSING_SIGNATURE_CONFIG.getErrorMeta().getException();
            }
            Signer signer = apiConfig.getSigner();
//             错误的sign
            if (!signer.checkSign(param, secret)) {
                throw ErrorEnum.ISV_INVALID_SIGNATURE.getErrorMeta().getException(param.fetchNameVersion());
            }
        } finally {
//             校验过程中会移除sign，这里需要重新设置进去
            param.setSign(clientSign);
        }
    }


    protected void checkFormat(ApiParam param) {
        String format = param.fetchFormat();
        boolean contains = FORMAT_LIST.contains(format.toLowerCase());

        if (!contains) {
            throw ErrorEnum.ISV_INVALID_FORMAT.getErrorMeta().getException(param.fetchNameVersion(), format);
        }
    }

    /**
     * 校验访问权限
     *
     * @param apiParam 参数
     */
    protected void checkPermission(ApiParam apiParam) {
        String routeId = apiParam.fetchNameVersion();
        TargetRoute targetRoute = RouteRepositoryContext.getRouteRepository().get(routeId);
        RouteDefinition routeDefinition = targetRoute.getRouteDefinition();
        boolean needCheckPermission = BooleanUtils.toBoolean(routeDefinition.getPermission());
        if (needCheckPermission) {
            String appKey = apiParam.fetchAppKey();
//            boolean hasPermission = isvRoutePermissionManager.hasPermission(appKey, routeId);
//            if (!hasPermission) {
//                throw ErrorEnum.ISV_ROUTE_NO_PERMISSIONS.getErrorMeta().getException();
//            }
        }
    }

    /**
     * 校验token
     *
     * @param apiParam 参数
     */
    protected void checkToken(ApiParam apiParam) {
        String routeId = apiParam.fetchNameVersion();
        TargetRoute targetRoute = RouteRepositoryContext.getRouteRepository().get(routeId);
        RouteDefinition routeDefinition = targetRoute.getRouteDefinition();
        boolean needToken = BooleanUtils.toBoolean(routeDefinition.getNeedToken());
        if (needToken) {
//            TokenValidator tokenValidator = ApiConfig.getInstance().getTokenValidator();
//            boolean rightToken = tokenValidator.validateToken(apiParam);
//            if (!rightToken) {
//                throw ErrorEnum.AOP_INVALID_APP_AUTH_TOKEN.getErrorMeta().getException();
//            }
        }
    }

}
