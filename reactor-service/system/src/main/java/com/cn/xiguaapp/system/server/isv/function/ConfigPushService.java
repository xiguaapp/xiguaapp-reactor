/*
 *        Copyright (C) <2018-2028>  <@author: xiguaapp @date: @today>
 *        Send: 1125698980@qq.com
 *        This program is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *        This program is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU General Public License for more details.
 *        You should have received a copy of the GNU General Public License
 *        along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cn.xiguaapp.system.server.isv.function;

import com.cn.xiguaapp.system.api.isv.entity.param.ChannelMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @author xiguaapp
 */
@Slf4j
@Component
public class ConfigPushService {

    private static final String GATEWAY_PUSH_URL = "http://%s/sop/configChannelMsg";
    private static final String API_GATEWAY_SERVICE_ID = "sop-gateway";

//    private static HttpTool httpTool = new HttpTool();

//    @Autowired
//    private ServerService serverService;

    public void publishConfig(String dataId, String groupId, ChannelMsg channelMsg) {
//        GatewayPushDTO gatewayPushDTO = new GatewayPushDTO(dataId, groupId, channelMsg);
//        ServiceSearchParam serviceSearchParam = new ServiceSearchParam();
//        serviceSearchParam.setServiceId(API_GATEWAY_SERVICE_ID);
//        List<ServiceInstanceVO> serviceInstanceList = serverService.listService(serviceSearchParam);
//        Collection<String> hostList = serviceInstanceList
//                .stream()
//                .filter(serviceInstanceVO -> StringUtils.isNotBlank(serviceInstanceVO.getInstanceId()))
//                .map(ServiceInstanceVO::getIpPort)
//                .collect(Collectors.toList());
//        this.pushByHost(hostList, gatewayPushDTO);
    }

//    private void pushByHost(Collection<String> hosts, GatewayPushDTO gatewayPushDTO) {
//        for (String host : hosts) {
//            String url = String.format(GATEWAY_PUSH_URL, host);
//            log.info("推送配置, dataId={}, groupId={}, operation={}， url={}",
//                    gatewayPushDTO.getDataId()
//                    , gatewayPushDTO.getGroupId()
//                    , gatewayPushDTO.getChannelMsg().getOperation()
//                    , url);
//            try {
//                String requestBody = JSON.toJSONString(gatewayPushDTO);
//                Map<String, String> header = new HashMap<>(8);
//                header.put("sign", buildRequestBodySign(requestBody, secret));
//                String resp = httpTool.requestJson(url, requestBody, header);
//                if (!"ok".equals(resp)) {
//                    throw new IOException(resp);
//                }
//            } catch (IOException e) {
//                log.error("nacos配置失败, dataId={}, groupId={}, operation={}， url={}",
//                        gatewayPushDTO.getDataId()
//                        , gatewayPushDTO.getGroupId()
//                        , gatewayPushDTO.getChannelMsg().getOperation()
//                        , url
//                        , e);
//                throw new BizException("推送配置失败");
//            }
//        }
//    }

    public static String buildRequestBodySign(String requestBody, String secret) {
        String signContent = secret + requestBody + secret;
        return DigestUtils.md5Hex(signContent);
    }

}
