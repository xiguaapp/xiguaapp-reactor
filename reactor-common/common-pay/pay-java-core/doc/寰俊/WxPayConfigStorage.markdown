`WxPayConfigStorage`继承自`BasePayConfigStorage` 用于存储维护支付宝账户信息



以下为`BasePayConfigStorage`的字段解释

```

    1. keyPrivate            应用私钥，rsa_private pkcs8格式 生成签名时使用

    2. keyPublic            支付平台公钥(签名校验使用)    

    3. notifyUrl             异步回调地址

    4. returnUrl             同步回调地址，支付完成后展示的页面

    5. signType                签名加密类型

    6. inputCharset            字符类型    

    7. payType                支付类型 aliPay 支付宝， wxPay微信..等等，扩展支付模块定义唯一

    8. msgType                消息来源类型

    9. accessToken            访问令牌 每次请求其他方法都要传入的值

    10. expiresTime            access token 到期时间时间戳

    11. accessTokenLock     授权码锁
 
   12. isTest                 是否为沙箱环境，默认为正式环境


```


以下为`WxPayConfigStorage`的字段解释

```

  1. appid          应用id;

  2. mchId          商户号 合作者id;

```

 ** 在[快速入门](微信_快速入门?parent=微信)的例子里我们使用的是`WxPayConfigStorage`。在正式生产环境中，你可以提供自己的实现，比如在集群环境下将这些信息存储到数据库或者缓存里进行动态调配 **

<br/>

