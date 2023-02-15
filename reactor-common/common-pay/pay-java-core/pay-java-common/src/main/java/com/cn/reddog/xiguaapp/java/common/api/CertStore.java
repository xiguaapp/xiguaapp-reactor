package com.cn.xiguaapp.xiguaapp.java.common.api;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiguaapp
 * @desc 证书存储类型
 * @since 1.0 14:30
 */
public interface CertStore {
    /**
     * 证书信息转化为对应的输入流
     *
     * @param cert 证书信息
     * @return 输入流
     * @throws IOException 找不到文件异常
     */
    InputStream getInputStream(Object cert) throws IOException;
}
