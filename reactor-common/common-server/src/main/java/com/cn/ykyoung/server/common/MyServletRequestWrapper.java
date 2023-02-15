package com.cn.ykyoung.server.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author xiguaapp
 * @desc 包装HttpServletRequest
 * @since 1.0 23:48
 */
@Slf4j
public class MyServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;
    private final Map<String, String[]> parameterMap;

    public MyServletRequestWrapper(HttpServletRequest request) {
        super(request);
        // 先缓存请求参数，不然获取不到，原因：
        // 调用getInputStream方法时，usingInputStream会变成true
        // 之后再调用request.getParameterMap()，就一直返回空map
        // 因此要先调用request.getParameterMap()缓存起来
        // 详见org.apache.catalina.connector.Request.parseParameters()中
        // if (usingInputStream || usingReader) {
        //                success = true;
        //                return;
        //  }
        parameterMap = request.getParameterMap();
        try {
            body = IOUtils.toByteArray(super.getInputStream());
        } catch (IOException e) {
            log.error("cache body error, url:{}",request.getRequestURI(), e);
            throw new RuntimeException("cache body error", e);
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ByteArrayStreamWrapper(body);
    }

    /**
     * byte数组流包装
     *
     * @author tanghc
     */
    public static class ByteArrayStreamWrapper extends ServletInputStream {

        private byte[] data;
        private int idx = 0;

        /**
         * Creates a new <code>ByteArrayStreamWrapper</code> instance.
         *
         * @param data a <code>byte[]</code> value
         */
        public ByteArrayStreamWrapper(byte[] data) {
            if (data == null) {
                data = new byte[0];
            }
            this.data = data;
        }

        @Override
        public int read() throws IOException {
            if (idx == data.length) {
                return -1;
            }
            // I have to AND the byte with 0xff in order to ensure that it is returned as an unsigned integer
            // the lack of this was causing a weird bug when manually unzipping gzipped request bodies
            return data[idx++] & 0xff;
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}
