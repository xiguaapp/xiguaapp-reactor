/*
 *
 *       Copyright (C) <2018-2028>  <@author: xiguaapp @date: 2020/12/7 下午7:11 >
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

package com.cn.ykyoung.server.utils;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文件上传工具类
 *
 * @author xiguaapp
 */
public class UploadUtil {

    /**
     * 获取上传文件
     *
     * @param request
     * @return
     */
    public static Collection<MultipartFile> getUploadFiles(HttpServletRequest request) {
        MultiValueMap<String, MultipartFile> fileMap = null;
        //检查form中是否有enctype="multipart/form-data"
        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().contains("multipart")) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            fileMap = multiRequest.getMultiFileMap();
        }
        return Optional.ofNullable(fileMap)
                .map(Map::entrySet)
                .map(entry -> entry.stream()
                        .flatMap(e -> e.getValue().stream())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
