全能第三方支付对接Java开发工具包.优雅的轻量级支付模块集成支付对接支付整合（微信,支付宝,银联,友店,富友,跨境支付paypal,payoneer(P卡派安盈)易极付）app,扫码,网页支付刷卡付条码付刷脸付转账红包服务商模式、支持多种支付类型多支付账户，支付与业务完全剥离，简单几行代码即可实现支付，简单快速完成支付模块的开发，可轻松嵌入到任何系统里 目前仅是一个开发工具包（即SDK），只提供简单Web实现，建议使用maven或gradle引用本项目即可使用本SDK提供的各种支付相关的功能


### 特性
    1. 不依赖任何 mvc 框架，依赖极少:httpclient，fastjson,log4j,com.google.zxing，项目精简，不用担心项目迁移问题
    2. 也不依赖 servlet，仅仅作为工具使用，可轻松嵌入到任何系统里（项目例子利用spring mvc的 @PathVariable进行，推荐使用类似的框架）
    3. 支付请求调用支持HTTP和异步、支持http代理，连接池
    4. 简单快速完成支付模块的开发
    5. 支持多种支付类型多支付账户扩展

### 本项目包含 3 个部分：

     1. pay-java-common  公共lib,支付核心与规范定义
     2. pay-java-demo  具体的支付demo
     3. pay-java-*  具体的支付实现库

### Maven配置
具体支付模块 "{module-name}" 为具体的支付渠道的模块名 pay-java-ali，pay-java-wx等

```xml
<dependency>
    <groupId>com.cn.xiguaapp</groupId>
    <artifactId>{module-name}</artifactId>
    <version>1.0.1.REALEASE</version>
</dependency>

```
###### 支付教程
[教程详情](doc)
