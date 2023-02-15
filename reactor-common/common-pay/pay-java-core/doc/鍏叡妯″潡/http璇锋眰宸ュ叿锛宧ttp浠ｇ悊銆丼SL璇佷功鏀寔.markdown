  com.cn.xiguaapp.pay.java.common.http 包下的对应类

---

### http代理、SSL证书配置信息


#####在某些环境下，应用程序必须通过代理才能够访问支付接口，SSL请求证书用户支付所需比如微信退款等


#### http代理  

    1. httpProxyHost        http代理地址

    2. httpProxyPort        代理端口

    3. authUsername         请求授权用户名（非必需，看代理服务器选择）

    4. authPassword         请求授权密码（非必需，看代理服务器选择）

    
#### SSL证书配置信息

    5. keystore         https请求所需的证书（PKCS12）,支持方式：1.文件路径(绝对路径)，2.证书文件信息串，3.证书输入流

    6. storePassword        证书对应的密码

    7. certStoreType        证书存储类型 #keystore 是否为https请求所需的证书（PKCS12）的地址,默认为地址，否则为证书信息串，文件流 

 `你可以在构造自己的PayConfigStorage的时候设置HttpConfigStorage对应的代理配置与SSL相关证书 `
  
#### http连接池相关
    8. maxTotal            最大连接数
    
    9. defaultMaxPerRoute  默认的每个路由的最大连接数
`这里这两个参数（maxTotal，defaultMaxPerRoute）同时设置时开启连接池，默认状态下不开启连接池，而是用一个支付账户一个连接的方式`

<br/>

<br/>

----

<br/>
#### http请求工具的：HttpRequestTemplate(请求模板的)讲解
<br/>


 ** 构造方法** 
```

    //默认的构造方法
    public HttpRequestTemplate();
    //创建一个带HTTP配置的请求模板
    public HttpRequestTemplate(HttpConfigStorage configStorage);

```

 **发起请求 以下列举几个方法，详情请[查看源码](pay-java-common/src/main/java/com/egzosn/pay/common/http/HttpRequestTemplate.java)**

```
 /**
     * get 请求
     * @param uri          请求地址
     * @param responseType 响应类型
     * @param uriVariables 用于匹配表达式
     * @param <T>          响应类型
     * @return 类型对象
     * <code>
     * Map<String, String> uriVariables = new HashMap<String, String>();<br>
     *
     * uriVariables.put("id", "1");<br>
     *
     * uriVariables.put("type", "APP");<br>
     *
     * getForObject("http://egan.in/pay/{id}/f/{type}", String.class, uriVariables)<br/>
     * </code>
     */
    public <T> T getForObject(String uri, Class<T> responseType, Map<String, ?> uriVariables);


    /**
     * get 请求
     * @param uri 请求地址
     * @param responseType 响应类型
     * @param uriVariables 用于匹配表达式
     * @param <T> 响应类型
     * @return 类型对象
     *
     * <code>
     *    getForObject("http://egan.in/pay/{id}/f/{type}", String.class, "1", "APP")
     * </code>
     */
    public <T> T getForObject(String uri, Class<T> responseType, Object... uriVariables);



    /**
     * post
     * @param uri 请求地址
     * @param request 请求参数
     * @param responseType 为响应类(需要自己依据响应格式来确定)
     * @param uriVariables 地址通配符对应的值
     * @param <T> 响应类型
     * @return 类型对象
     */
    public <T> T postForObject(String uri, Object request, Class<T> responseType, Object... uriVariables);
    

``` 
--- 
#### http请求工具的：HttpRequestTemplate(请求模板的)使用
<br/>


 **创建一个请求模板** 

```
    HttpRequestTemplate template = new  HttpRequestTemplate(); 

```

 **创建一个带HTTP配置的请求模板**

```
    //http代理SSL配置信息
    HttpConfigStorage configStorage = new HttpConfigStorage();
    
     /* 网路代理配置 根据需求进行设置**/
        //http代理地址
        httpConfigStorage.setHttpProxyHost("代理地址");
        //代理端口
        httpConfigStorage.setHttpProxyPort(代理端口);
        //代理用户名
        httpConfigStorage.setHttpProxyUsername("user");
        //代理密码
        httpConfigStorage.setHttpProxyPassword("password");

        /* 网路代理配置 根据需求进行设置**/

         /* 网络请求ssl证书 根据需求进行设置**/
        //设置ssl证书路径
        // httpConfigStorage.setKeystore(WxPayController.class.getResourceAsStream("/证书文件"));
        httpConfigStorage.setKeystore("证书绝对路径");
        //设置ssl证书对应的密码
        httpConfigStorage.setStorePassword("证书对应的密码");
        //设置ssl证书对应的存储方式，这里默认为文件地址
        httpConfigStorage.setCertStoreType(CertStoreType.PATH);

        /* /网络请求ssl证书**/

    //创建一个带http代理、SSL证书支持请求模板
    HttpRequestTemplate template = new HttpRequestTemplate(configStorage);

```

 **发起get请求** 

```
    //返回一个String类型的结果集
    String result = template.getForObject("http://egan.in/pay/{id}/f/{type}", String.class,"1","APP")


     Map<String, String> uriVariables = new HashMap<String, String>(2);
     uriVariables.put("id", "1");
     uriVariables.put("type", "APP");
     String result = getForObject("http://egan.in/pay/{id}/f/{type}", String.class, uriVariables)；
```

 **发起post请求(与get方式一样外增加一个请求内容对象)**

```
    
    //第一种方式： 发送一个字符串，返回一个String类型的结果集
    String result = template.postForObject("http://egan.in/pay/{id}/f/{type}", "name=张三&age=16", String.class,"1","APP")

    //第二种方式： 发送一个Map，返回一个Order类型的结果集（与第一种方式原理一至）
    Map<String, String> request = new HashMap<String, String>(2);
    request.put("id", "1");
    request.put("type", "APP");
    //这里的结果集为非字符串那么返回信息必须是能进行转化为实体结果集,比如JSON，XML
    Order result = template.postForObject("http://egan.in/pay/1/f/APP", request, Order.class)
 
    //第三种方式： 发送一个自定义对象，返回一个String类型的结果集，这种方式与前面两种方式不一样的点在于是将请求对象序列化成json发送并改变请求内容类型为json  ContentType.APPLICATION_JSON
    PayOrder request = new PayOrder();
    request.setSubject("商品标题");
    String result = template.postForObject("http://egan.in/pay/1/f/APP", request, String.class)
 

```

#### HttpStringEntity请求实体的使用,可设置请求头的扩展
 **以下列举几种使用方式** 

key-value的形式,等同于上面post请求的方式二，Header构造式参数设置请求头
```
 //请求参数
Map<String, Object> request = new HashMap<String, Object>(2);
request.put("id", "1");
request.put("type", "APP");
//请求头信息
Header header = new BasicHeader("p1","请求头参数");
//创建请求实体
HttpStringEntity entity = new HttpStringEntity(request, header);
JSONObject response = getHttpRequestTemplate().postForObject("http://egan.in/pay/1/f/APP", entity, JSONObject.class);

```


json的形式,Map式请求头集设置
```
//请求参数
Map<String, Object> request = new HashMap<String, Object>(2);
request.put("id", "1");
request.put("type", "APP");

//创建请求实体
HttpStringEntity entity = new HttpStringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON);
//创建请求头信息集
Map<String, String> headers = new HashMap<>(2);
headers.put("p1","请求头参数1");
headers.put("p2","请求头参数2");
//设置请求头
entity.setHeaders(headers);
JSONObject response = getHttpRequestTemplate().postForObject("http://egan.in/pay/1/f/APP", entity, JSONObject.class);
```
`简单讲述了两种的方式，具体使用请在开发时调用对应方法查看`

