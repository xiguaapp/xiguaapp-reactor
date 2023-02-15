/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/8 下午4:36 >
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

package com.cn.xiguaapp.common.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.cn.xiguaapp.common.gateway.bean.GatewayConstants;
import com.cn.xiguaapp.common.gateway.bean.UploadContext;
import com.cn.xiguaapp.common.gateway.param.ApiUploadContext;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author xiguaapp
 */
public class RequestUtil {

    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

    private RequestUtil() {
    }

    public static final String MULTIPART = "multipart/";

    private static final String UTF8 = "UTF-8";
    private static final String IP_UNKNOWN = "unknown";
    private static final String IP_LOCAL = "127.0.0.1";
    private static final int IP_LEN = 15;

    /**
     * 将get类型的参数转换成map，
     *
     * @param query charset=utf-8&biz_content=xxx
     * @return 返回map参数
     */
    public static Map<String, String> parseQueryToMap(String query) {
        if (query == null) {
            return Collections.emptyMap();
        }
        String[] queryList = StringUtils.split(query, '&');
        Map<String, String> params = new HashMap<>(16);
        for (String param : queryList) {
            String[] paramArr = param.split("\\=");
            if (paramArr.length == 2) {
                try {
                    params.put(paramArr[0], URLDecoder.decode(paramArr[1], UTF8));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else if (paramArr.length == 1) {
                params.put(paramArr[0], "");
            }
        }
        return params;
    }

    /**
     * 将map参数转换成查询参数
     *
     * @return 返回aa=1&b=c...
     */
    public static String convertMapToQueryString(Map<String, ?> apiParam) {
        List<String> list = new ArrayList<>(apiParam.size());
        try {
            for (Map.Entry<String, ?> entry : apiParam.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    Collection collection = (Collection) value;
                    for (Object el : collection) {
                        list.add(key + "=" + URLEncoder.encode(String.valueOf(el), GatewayConstants.UTF8));
                    }
                } else {
                    list.add(key + "=" + URLEncoder.encode(String.valueOf(value), GatewayConstants.UTF8));
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("字符集不支持", e);
        }
        return org.apache.commons.lang.StringUtils.join(list, "&");
    }

    /**
     * request中的参数转换成map
     *
     * @param request request对象
     * @return 返回参数键值对
     */
    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap == null || paramMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> retMap = new HashMap<>(paramMap.size() * 2);

        Set<Map.Entry<String, String[]>> entrySet = paramMap.entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            if (values.length >= 1) {
                retMap.put(name, values[0]);
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }

    /**
     * 获取文件上传表单中的字段，不包括文件，请求类型是multipart/form-data
     *
     * @param request
     * @return 返回表单中的字段内容
     */
    public static Map<String, String> convertMultipartRequestToMap(HttpServletRequest request) {
        // 创建一个文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        Map<String, String> params = new HashMap<>(16);
        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    params.put(fileItem.getFieldName(), fileItem.getString(UTF8));
                }
            }
        } catch (Exception e) {
            log.error("参数解析错误", e);
        }
        return params;
    }

    /**
     * 转换json请求到Map，
     *
     * @param request 请求类型为application/json的Request
     * @return 返回Map
     */
    public static Map<String, Object> convertJsonRequestToMap(HttpServletRequest request) {
        try {
            String text = getText(request);
            return JSON.parseObject(text);
        } catch (IOException e) {
            log.error("解析json请求失败", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 是否是文件上传请求
     *
     * @param request 请求
     * @return true：是
     */
    public static boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        // Don't use this filter on GET method
        if (contentType == null) {
            return false;
        }
        return contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART);
    }


    public static String getText(HttpServletRequest request) throws IOException {
        return IOUtils.toString(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
    }

    /**
     * 获取客户端IP
     *
     * @param request request
     * @return 返回ip
     */
    public static String getIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (IP_LOCAL.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    // ignore
                }
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > IP_LEN) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获取客户端真实ip
     * @param request request
     * @return 返回ip
     */
    public static String getIP(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ipAddress = headers.getFirst("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(request.getRemoteAddress())
                    .map(address -> address.getAddress().getHostAddress())
                    .orElse("");
            if (IP_LOCAL.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    // ignore
                }
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > IP_LEN) {
            int index = ipAddress.indexOf(",");
            if (index > 0) {
                ipAddress = ipAddress.substring(0, index);
            }
        }
        return ipAddress;
    }

    /**
     * 获取上传文件内容
     *
     * @param request request
     * @return 返回文件内容和表单内容
     */
    public static UploadInfo getUploadInfo(HttpServletRequest request) {
        if (request instanceof StandardMultipartHttpServletRequest) {
            return getUploadInfo((StandardMultipartHttpServletRequest)request);
        }
        UploadInfo uploadInfo = new UploadInfo();
        // 创建一个文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        Map<String, String> uploadParams = new HashMap<>(16);
        UploadContext uploadContext = null;
        try {
            List<MultipartFile> multipartFileList = new ArrayList<>(16);
            List<FileItem> fileItems = upload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {
                    uploadParams.put(fileItem.getFieldName(), fileItem.getString(GatewayConstants.UTF8));
                } else {
                    multipartFileList.add(new CommonsMultipartFile(fileItem));
                }
            }
            if (multipartFileList.size() > 0) {
                Map<String, List<MultipartFile>> multipartFileMap = multipartFileList
                        .stream()
                        .collect(Collectors.groupingBy(MultipartFile::getName));
                uploadContext = new ApiUploadContext(multipartFileMap);
            }
            uploadInfo.setUploadParams(uploadParams);
            uploadInfo.setUploadContext(uploadContext);
        } catch (Exception e) {
            log.error("参数解析错误", e);
        }
        return uploadInfo;
    }

    public static UploadInfo getUploadInfo(StandardMultipartHttpServletRequest request) {
        UploadInfo uploadInfo = new UploadInfo();
        Map<String, String> uploadParams = new HashMap<>(16);
        request.getParameterMap().forEach((key, value)-> uploadParams.put(key, value[0]));
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> multipartFileList = new ArrayList<>(10);
        for (String key : multiFileMap.keySet()) {
            multipartFileList.addAll(multiFileMap.get(key));
        }
        Map<String, List<MultipartFile>> multipartFileMap = multipartFileList
                .stream()
                .collect(Collectors.groupingBy(MultipartFile::getName));
        UploadContext uploadContext = new ApiUploadContext(multipartFileMap);

        uploadInfo.setUploadParams(uploadParams);
        uploadInfo.setUploadContext(uploadContext);
        return uploadInfo;
    }

    public static void checkResponseBody(String responseBody, String sign, String secret) throws Exception {
        if (sign == null) {
            throw new Exception("签名不存在");
        }
        String signContent = secret + responseBody + secret;
        String clientSign = DigestUtils.md5Hex(signContent);
        if (!sign.equals(clientSign)) {
            throw new Exception("签名错误");
        }
    }

    @Data
    public static class UploadInfo {
        private Map<String, String> uploadParams;
        private UploadContext uploadContext;
    }

}
