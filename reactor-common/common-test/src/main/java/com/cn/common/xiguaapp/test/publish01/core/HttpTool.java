package com.cn.common.xiguaapp.test.publish01.core;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具，基于OKHTTP3
 * @author xiguaapp
 */
public class HttpTool {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private Map<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

    private OkHttpClient httpClient;

    public HttpTool() {
        this(new HttpToolConfig());
    }

    public HttpTool(HttpToolConfig httpToolConfig) {
        this.initHttpClient(httpToolConfig);
    }

    protected void initHttpClient(HttpToolConfig httpToolConfig) {
        httpClient = new OkHttpClient.Builder()
                // 设置链接超时时间，默认10秒
                .connectTimeout(httpToolConfig.connectTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(httpToolConfig.readTimeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(httpToolConfig.writeTimeoutSeconds, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();
    }

    @Data
    public static class HttpToolConfig {
        /**
         * 请求超时时间
         */
        private int connectTimeoutSeconds = 10;
        /**
         * http读取超时时间
         */
        private int readTimeoutSeconds = 10;
        /**
         * http写超时时间
         */
        private int writeTimeoutSeconds = 10;
    }

    /**
     * get请求
     *
     * @param url
     * @param header
     * @return
     * @throws IOException
     */
    public String get(String url, Map<String, String> header) throws IOException {
        Request.Builder builder = new Request.Builder().url(url).get();
        // 添加header
        addHeader(builder, header);

        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 提交表单
     *
     * @param url    url
     * @param form   参数
     * @param header header
     * @param method 请求方式，post，get等
     * @return
     * @throws IOException
     */
    public String request(String url, Map<String, ?> form, Map<String, String> header, HTTPMethod method) throws IOException {
        Request.Builder requestBuilder = buildRequestBuilder(url, form, method);
        // 添加header
        addHeader(requestBuilder, header);

        Request request = requestBuilder.build();
        Response response = httpClient
                .newCall(request)
                .execute();
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

    /**
     * 下载文件
     *
     * @param url    url
     * @param form   参数
     * @param header header
     * @param method 请求方式，post，get等
     * @return
     * @throws IOException
     */
    public InputStream download(String url, Map<String, ?> form, Map<String, String> header, HTTPMethod method) throws IOException {
        Request.Builder requestBuilder = buildRequestBuilder(url, form, method);
        // 添加header
        addHeader(requestBuilder, header);

        Request request = requestBuilder.build();
        Response response = httpClient
                .newCall(request)
                .execute();
        return response.body().byteStream();
    }

    /**
     * 请求json数据，contentType=application/json
     * @param url 请求路径
     * @param json json数据
     * @param header header
     * @return 返回响应结果
     * @throws IOException
     */
    public String requestJson(String url, String json, Map<String, String> header) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        // 添加header
        addHeader(requestBuilder, header);

        Request request = requestBuilder.build();
        Response response = httpClient
                .newCall(request)
                .execute();
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

    /**
     * 请求json数据，contentType=application/json
     * @param url 请求路径
     * @param json json数据
     * @param header header
     * @return 返回响应结果
     * @throws IOException
     */
    public InputStream downloadJson(String url, String json, Map<String, String> header) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        // 添加header
        addHeader(requestBuilder, header);

        Request request = requestBuilder.build();
        Response response = httpClient
                .newCall(request)
                .execute();
        return response.body().byteStream();
    }

    public static Request.Builder buildRequestBuilder(String url, Map<String, ?> form, HTTPMethod method) {
        switch (method) {
            case GET:
                return new Request.Builder()
                        .url(buildHttpUrl(url, form))
                        .get();
            case HEAD:
                return new Request.Builder()
                        .url(buildHttpUrl(url, form))
                        .head();
            case PUT:
                return new Request.Builder()
                        .url(url)
                        .put(buildFormBody(form));
            case DELETE:
                return new Request.Builder()
                        .url(url)
                        .delete(buildFormBody(form));
            default:
                return new Request.Builder()
                        .url(url)
                        .post(buildFormBody(form));
        }
    }

    public static HttpUrl buildHttpUrl(String url, Map<String, ?> form) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, ?> entry : form.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return urlBuilder.build();
    }

    public static FormBody buildFormBody(Map<String, ?> form) {
        FormBody.Builder paramBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        for (Map.Entry<String, ?> entry : form.entrySet()) {
            paramBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return paramBuilder.build();
    }

    /**
     * 提交表单，并且上传文件
     *
     * @param url
     * @param form
     * @param header
     * @param files
     * @return
     * @throws IOException
     */
    public String requestFile(String url, Map<String, ?> form, Map<String, String> header, List<UploadFile> files)
            throws IOException {
        // 创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);

        for (UploadFile uploadFile : files) {
            // 请求的名字
            bodyBuilder.addFormDataPart(uploadFile.getName(),
                    // 文件的文字，服务器端用来解析的
                    uploadFile.getFileName(),
                    // 创建RequestBody，把上传的文件放入
                    RequestBody.create(null, uploadFile.getFileData())
            );
        }

        for (Map.Entry<String, ?> entry : form.entrySet()) {
            bodyBuilder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
        }

        RequestBody requestBody = bodyBuilder.build();

        Request.Builder builder = new Request.Builder().url(url).post(requestBody);

        // 添加header
        addHeader(builder, header);

        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

    /**
     * 上传文件，然后下载文件
     *
     * @param url
     * @param form
     * @param header
     * @param files
     * @return
     * @throws IOException
     */
    public InputStream downloadByRequestFile(String url, Map<String, ?> form, Map<String, String> header, List<UploadFile> files)
            throws IOException {
        // 创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);

        for (UploadFile uploadFile : files) {
            // 请求的名字
            bodyBuilder.addFormDataPart(uploadFile.getName(),
                    // 文件的文字，服务器端用来解析的
                    uploadFile.getFileName(),
                    // 创建RequestBody，把上传的文件放入
                    RequestBody.create(null, uploadFile.getFileData())
            );
        }

        for (Map.Entry<String, ?> entry : form.entrySet()) {
            bodyBuilder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
        }

        RequestBody requestBody = bodyBuilder.build();

        Request.Builder builder = new Request.Builder().url(url).post(requestBody);

        // 添加header
        addHeader(builder, header);

        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        return response.body().byteStream();
    }

    /**
     * 请求数据
     *
     * @param url    请求url
     * @param form   请求数据
     * @param header header
     * @return 返回Response
     * @throws IOException
     */
    public Response requestForResponse(String url, Map<String, ?> form, Map<String, String> header, HTTPMethod method) throws IOException {
        Request.Builder requestBuilder = buildRequestBuilder(url, form, method);
        // 添加header
        addHeader(requestBuilder, header);

        Request request = requestBuilder.build();
        return httpClient
                .newCall(request)
                .execute();
    }

    /**
     * 下载文件
     *
     * @param url    请求url
     * @param form   请求数据
     * @param header header
     * @return 返回文件流
     * @throws IOException
     */
    public InputStream downloadFile(String url, Map<String, ?> form, Map<String, String> header) throws IOException {
        Request.Builder requestBuilder = buildRequestBuilder(url, form, HTTPMethod.GET);
        // 添加header
        addHeader(requestBuilder, header);

        Request request = requestBuilder.build();
        Response response = httpClient
                .newCall(request)
                .execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            return body == null ? null : body.byteStream();
        }
        return null;
    }

    private void addHeader(Request.Builder builder, Map<String, String> header) {
        if (header != null) {
            Set<Map.Entry<String, String>> entrySet = header.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                builder.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    public void setCookieStore(Map<String, List<Cookie>> cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public enum HTTPMethod {
        /** http GET */
        GET,
        /** http POST */
        POST,
        /** http PUT */
        PUT,
        /** http HEAD */
        HEAD,
        /** http DELETE */
        DELETE;

        HTTPMethod() {
        }

        public String value() {
            return this.name();
        }

        public static HTTPMethod fromValue(String v) {
            return valueOf(v.toUpperCase());
        }
    }

    /**
     * 文件上传类
     * @author xiguaapp
     */
    @Getter
    @Setter
    public static class UploadFile implements Serializable {
        private static final long serialVersionUID = -1100614660944996398L;

        /**
         * @param name 表单名称，不能重复
         * @param file 文件
         * @throws IOException
         */
        public UploadFile(String name, File file) throws IOException {
            this(name, file.getName(), FileUtil.toBytes(file));
        }

        /**
         * @param name 表单名称，不能重复
         * @param fileName 文件名
         * @param input 文件流
         * @throws IOException
         */
        public UploadFile(String name, String fileName, InputStream input) throws IOException {
            this(name, fileName, FileUtil.toBytes(input));
        }

        /**
         * @param name 表单名称，不能重复
         * @param fileName 文件名
         * @param fileData 文件数据
         */
        public UploadFile(String name, String fileName, byte[] fileData) {
            super();
            this.name = name;
            this.fileName = fileName;
            this.fileData = fileData;
            this.md5 = DigestUtils.md5Hex(fileData);
        }

        private String name;
        private String fileName;
        private byte[] fileData;
        private String md5;

    }

    public static class FileUtil {

        /**
         * The default buffer size to use.
         */
        private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
        private static final int EOF = -1;

        /**
         * 将文件流转换成byte[]
         * @param input
         * @return
         * @throws IOException
         */
        public static byte[] toBytes(InputStream input) throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int n = 0;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            while (EOF != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }

        /**
         * 将文件转换成数据流
         * @param file 文件
         * @return 返回数据流
         * @throws IOException
         */
        public static byte[] toBytes(File file) throws IOException {
            if (file.exists()) {
                if (file.isDirectory()) {
                    throw new IOException("File '" + file + "' exists but is a directory");
                }
                if (!file.canRead()) {
                    throw new IOException("File '" + file + "' cannot be read");
                }
            } else {
                throw new FileNotFoundException("File '" + file + "' does not exist");
            }
            InputStream input = null;
            try {
                input = new FileInputStream(file);
                return toBytes(input);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ioe) {
                }
            }
        }
    }
}
