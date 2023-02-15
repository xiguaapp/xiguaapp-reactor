package com.cn.common.xiguaapp.test.publish01.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.common.xiguaapp.test.publish01.core.Client;
import com.cn.common.xiguaapp.test.publish01.core.HttpTool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 涵盖所有测试情况，发版前运行这个类，确保功能没有问题。
 *
 * @author tanghc
 */
public class AllInOneTest extends TestBase {

    String url = "http://localhost:8081";
    String appId = "2019032617262200001";
    String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXJv1pQFqWNA/++OYEV7WYXwexZK/J8LY1OWlP9X0T6wHFOvxNKRvMkJ5544SbgsJpVcvRDPrcxmhPbi/sAhdO4x2PiPKIz9Yni2OtYCCeaiE056B+e1O2jXoLeXbfi9fPivJZkxH/tb4xfLkH3bA8ZAQnQsoXA0SguykMRZntF0TndUfvDrLqwhlR8r5iRdZLB6F8o8qXH6UPDfNEnf/K8wX5T4EB1b8x8QJ7Ua4GcIUqeUxGHdQpzNbJdaQvoi06lgccmL+PHzminkFYON7alj1CjDN833j7QMHdPtS9l7B67fOU/p2LAAkPMtoVBfxQt9aFj7B8rEhGCz02iJIBAgMBAAECggEARqOuIpY0v6WtJBfmR3lGIOOokLrhfJrGTLF8CiZMQha+SRJ7/wOLPlsH9SbjPlopyViTXCuYwbzn2tdABigkBHYXxpDV6CJZjzmRZ+FY3S/0POlTFElGojYUJ3CooWiVfyUMhdg5vSuOq0oCny53woFrf32zPHYGiKdvU5Djku1onbDU0Lw8w+5tguuEZ76kZ/lUcccGy5978FFmYpzY/65RHCpvLiLqYyWTtaNT1aQ/9pw4jX9HO9NfdJ9gYFK8r/2f36ZE4hxluAfeOXQfRC/WhPmiw/ReUhxPznG/WgKaa/OaRtAx3inbQ+JuCND7uuKeRe4osP2jLPHPP6AUwQKBgQDUNu3BkLoKaimjGOjCTAwtp71g1oo+k5/uEInAo7lyEwpV0EuUMwLA/HCqUgR4K9pyYV+Oyb8d6f0+Hz0BMD92I2pqlXrD7xV2WzDvyXM3s63NvorRooKcyfd9i6ccMjAyTR2qfLkxv0hlbBbsPHz4BbU63xhTJp3Ghi0/ey/1HQKBgQC2VsgqC6ykfSidZUNLmQZe3J0p/Qf9VLkfrQ+xaHapOs6AzDU2H2osuysqXTLJHsGfrwVaTs00ER2z8ljTJPBUtNtOLrwNRlvgdnzyVAKHfOgDBGwJgiwpeE9voB1oAV/mXqSaUWNnuwlOIhvQEBwekqNyWvhLqC7nCAIhj3yvNQKBgQCqYbeec56LAhWP903Zwcj9VvG7sESqXUhIkUqoOkuIBTWFFIm54QLTA1tJxDQGb98heoCIWf5x/A3xNI98RsqNBX5JON6qNWjb7/dobitti3t99v/ptDp9u8JTMC7penoryLKK0Ty3bkan95Kn9SC42YxaSghzqkt+uvfVQgiNGQKBgGxU6P2aDAt6VNwWosHSe+d2WWXt8IZBhO9d6dn0f7ORvcjmCqNKTNGgrkewMZEuVcliueJquR47IROdY8qmwqcBAN7Vg2K7r7CPlTKAWTRYMJxCT1Hi5gwJb+CZF3+IeYqsJk2NF2s0w5WJTE70k1BSvQsfIzAIDz2yE1oPHvwVAoGAA6e+xQkVH4fMEph55RJIZ5goI4Y76BSvt2N5OKZKd4HtaV+eIhM3SDsVYRLIm9ZquJHMiZQGyUGnsvrKL6AAVNK7eQZCRDk9KQz+0GKOGqku0nOZjUbAu6A2/vtXAaAuFSFx1rUQVVjFulLexkXR3KcztL1Qu2k5pB6Si0K/uwQ=";

    private final Client client = new Client(url, appId, privateKey, AllInOneTest::assertResult);

    /**
     * 以get方式提交
     */
    public void testGet() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get")
                .version("1.0")
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .httpMethod(HttpTool.HTTPMethod.GET);

        client.execute(requestBuilder);
    }

    /**
     * 以表单方式提交(application/x-www-form-urlencoded)
     */
    public void testPostForm() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get")
                .version("1.0")
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .httpMethod(HttpTool.HTTPMethod.POST);

        client.execute(requestBuilder);
    }

    public void testSystemParam() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.system.param.get")
                .version("1.0")
                .appAuthToken("123123")
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .httpMethod(HttpTool.HTTPMethod.GET);

        client.execute(requestBuilder);
    }

    /**
     * 以json方式提交(application/json)
     */
    public void testPostJSON() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get")
                .version("1.0")
                // 以json方式提交
                .postJson(true)
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"));

        client.execute(requestBuilder);
    }

    /**
     * 以get方式提交
     */
    public void testGet_v2() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get")
                .version("2.0")
                .bizContent(new BizContent().add("id", "2").add("name", "葫芦娃2"))
                .httpMethod(HttpTool.HTTPMethod.GET);

        client.execute(requestBuilder);
    }

    /**
     * 忽略验证,不校验签名，只需传接口名、版本号、业务参数
     */
    public void testIgnoreSign() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get.ignore")
                .version("1.0")
                .ignoreSign(true)
                .bizContent(new BizContent().add("id", "222").add("name", "忽略222"))
                .httpMethod(HttpTool.HTTPMethod.POST);

        client.execute(requestBuilder);
    }

    /**
     * JSR-303参数校验
     */
    public void testJSR303() {
        Client client = new Client(url, appId, privateKey);
        Map<String, String> headers = new HashMap<>(8);
        headers.put("accept-language", "zh-CN");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("goods.add")
                .version("1.0")
                .header(headers)
                .bizContent(new BizContent()
                        .add("goods_name", "iphone6")
                        .add("goods_remark", "iphone6")
                        .add("goods_comment", "1"))
                .httpMethod(HttpTool.HTTPMethod.POST)
                .callback((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    JSONObject jsonObject = JSON.parseObject(responseData);
                    String sub_msg = jsonObject.getJSONObject(requestInfo.getDataNode()).getString("sub_msg");
                    Assert.assertEquals(sub_msg, "商品评论长度必须在3和20之间");
                });

        client.execute(requestBuilder);
    }

    /**
     * 测试是否有权限访问，可在sop-admin中设置权限
     */
    public void testPermission() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get.permission")
                .version("1.0")
                .bizContent(new BizContent())
                .httpMethod(HttpTool.HTTPMethod.GET)
                .callback((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    String node = requestInfo.getDataNode();
                    JSONObject jsonObject = JSON.parseObject(responseData).getJSONObject(node);
                    Assert.assertEquals("isv.route-no-permissions", jsonObject.getString("sub_code"));
                });

        client.execute(requestBuilder);
    }

    /**
     * 演示文件上传
     */
    public void testFile() {
        Client client = new Client(url, appId, privateKey);
        String root = System.getProperty("user.dir");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("file.upload")
                .version("1.0")
                .bizContent(new BizContent().add("remark", "test file upload"))
                // 添加文件
                .addFile("file1", new File(root + "/src/main/resources/file1.txt"))
                .addFile("file2", new File(root + "/src/main/resources/file2.txt"))
                .callback((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    JSONObject jsonObject = JSON.parseObject(responseData);
                    JSONObject data = jsonObject.getJSONObject(requestInfo.getDataNode());
                    Assert.assertEquals(data.getString("code"), "10000");
                    Assert.assertEquals(data.getJSONArray("files").size(), 2);
                })
                ;

        client.execute(requestBuilder);
    }

    /**
     * 演示文件上传2
     */
    public void testFile2() {
        Client client = new Client(url, appId, privateKey);
        String root = System.getProperty("user.dir");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("file.upload2")
                .version("1.0")
                .bizContent(new BizContent().add("remark", "test file upload"))
                // 添加文件
                .addFile("file1", new File(root + "/src/main/resources/file1.txt"))
                .addFile("file2", new File(root + "/src/main/resources/file2.txt"))
                .callback((requestInfo, responseData) -> {
                    JSONObject jsonObject = JSON.parseObject(responseData);
                    JSONObject data = jsonObject.getJSONObject(requestInfo.getDataNode());
                    Assert.assertEquals(data.getString("code"), "10000");
                    Assert.assertEquals(data.getJSONArray("files").size(), 2);
                })
                ;

        client.execute(requestBuilder);
    }

    /**
     * 演示文件上传
     */
    public void testFile3() {
        Client client = new Client(url, appId, privateKey);
        String root = System.getProperty("user.dir");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("file.upload3")
                .version("1.0")
                .bizContent(new BizContent().add("remark", "test file upload"))
                // 添加文件
                .addFile("image", new File(root + "/src/main/resources/img.png"))
                .callback((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    JSONObject jsonObject = JSON.parseObject(responseData);
                    JSONObject data = jsonObject.getJSONObject(requestInfo.getDataNode());
                    Assert.assertEquals(data.getString("code"), "10000");
                })
                ;

        client.execute(requestBuilder);
    }

    /**
     * 演示大文件上传，先配置网关参数：spring.servlet.multipart.max-file-size=1MB
     */
    public void testBigFile() {
        Client client = new Client(url, appId, privateKey);
        String root = System.getProperty("user.dir");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("file.upload3")
                .version("1.0")
                .bizContent(new BizContent().add("remark", "test file upload"))
                // 添加文件
                .addFile("image", new File(root + "/src/main/resources/large_img.png"))
                .callback((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    JSONObject jsonObject = JSON.parseObject(responseData);
                    JSONObject data = jsonObject.getJSONObject(requestInfo.getDataNode());
                    Assert.assertEquals(data.getString("sub_code"), "isv.invalid-file-size");
                })
                ;

        client.execute(requestBuilder);
    }

    /**
     * 传递header
     */
    public void testHeader() {
        Map<String, String> header = new HashMap<>(8);
        header.put("token", "aaaabbbb");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("test.head")
                .version("1.0")
                .header(header)
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .httpMethod(HttpTool.HTTPMethod.GET);

        client.execute(requestBuilder);
    }

    /**
     * 测试post提交大文本内容
     * @throws IOException
     */
    public void testLargeBody() throws IOException {
        String root = System.getProperty("user.dir");
        // 这个文件有600KB的数据
        File file = new File(root + "/src/main/resources/large_data.txt");
        String fileContent = FileUtils.readFileToString(file, String.valueOf(StandardCharsets.UTF_8));
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get.large")
                .version("1.0")
                .bizContent(new BizContent().add("content", fileContent))
                .httpMethod(HttpTool.HTTPMethod.POST);

        client.execute(requestBuilder);
    }

    /**
     * 下载文件
     */
    public void testDownloadFile() throws IOException {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("file.download")
                .version("1.0")
                .bizContent(new BizContent().add("id",1).add("name","Jim"))
                .httpMethod(HttpTool.HTTPMethod.GET);

        // 文件流
        InputStream download = client.download(requestBuilder);
        String content = IOUtils.toString(download, String.valueOf(StandardCharsets.UTF_8));
        System.out.println("下载文件内容：" + content);
        Assert.assertEquals(content, "spring.profiles.active=dev");
    }

    public void testToken() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get.token")
                .version("1.0")
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .appAuthToken("asdfasdfadsf")
                .httpMethod(HttpTool.HTTPMethod.GET);

        client.execute(requestBuilder);
    }

    /**
     * 国际化测试，返回英文错误
     */
    public void testLanguage() {
        // Accept-Language
        Map<String, String> header = new HashMap<>(4);
        header.put("Accept-Language", "en-US");
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.get==") // 制造错误的接口名
                .version("1.0")
                .header(header)
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .httpMethod(HttpTool.HTTPMethod.GET)
                .callback((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    String node = requestInfo.getDataNode();
                    JSONObject jsonObject = JSON.parseObject(responseData).getJSONObject(node);
                    Assert.assertEquals("Nonexistent method name", jsonObject.getString("sub_msg"));
                });

        client.execute(requestBuilder);
    }

    /**
     * 测试返回大json数据
     */
    public void testLargeResponseJson() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("bigdata.get")
                .version("1.0")
                .bizContent(new BizContent().add("id", "1").add("name", "葫芦娃"))
                .httpMethod(HttpTool.HTTPMethod.GET)
                .callback((requestInfo, responseData) -> {
                    int size = Optional.ofNullable(JSON.parseObject(responseData))
                            .flatMap(jsonObject -> Optional.ofNullable(jsonObject.getJSONObject("bigdata_get_response")))
                            .flatMap(jsonObject -> Optional.ofNullable(jsonObject.getJSONArray("data")))
                            .map(JSONArray::size)
                            .orElse(0);
                    Assert.assertEquals(size, 2000);
                });

        client.execute(requestBuilder);
    }

    /**
     * 单个参数绑定
     */
    public void testBindParam() {
        Client.RequestBuilder requestBuilder = new Client.RequestBuilder()
                .method("story.param.bind")
                .version("1.0")
                .addSystemParam("id", "1")
                .addSystemParam("name", "葫芦娃")
                .httpMethod(HttpTool.HTTPMethod.GET)
                .callback(((requestInfo, responseData) -> {
                    System.out.println(responseData);
                    Assert.assertTrue(responseData.contains("参数绑定：id:1, name:葫芦娃"));
                }));

        client.execute(requestBuilder);
    }

    static class BizContent extends HashMap<String, Object> {
        public BizContent add(String key, Object value) {
            this.put(key, value);
            return this;
        }
    }

    public static void assertResult(Client.RequestInfo requestInfo, String responseData) {
        System.out.println(responseData);
        String method = requestInfo.getMethod();
        if (method == null) {
            return;
        }
        String node = requestInfo.getDataNode();
        JSONObject jsonObject = JSON.parseObject(responseData).getJSONObject(node);
        String code = Optional.ofNullable(jsonObject).map(json -> json.getString("code")).orElse("20000");
        Assert.assertEquals("10000", code);
    }

}
