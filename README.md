# xiguaapp-reactor
<p align="center">
  <a target="_blank" href="https://nacos.io/en-us/"><img src="https://img.shields.io/badge/Nacos-blue.svg" alt="Nacos"></a>
  <a><img src="https://img.shields.io/badge/Spring%20Cloud-%20Hoxton.SR8-brightgreen.svg" alt="SpringCloud"></a>
  <a><img src="https://img.shields.io/badge/Spring%20Boot-2.3.3.RELEASE-brightgreen.svg" alt="SpringBoot"></a>
  <a><img src="https://img.shields.io/badge/Spring%20cloud%20alibaba-2.2.3.RELEASE-brightgreen.svg" alt="SpringBoot"></a>
  <a><img src="https://img.shields.io/badge/Spring%20Cloud%20function-%20Hoxton.SR8-brightgreen.svg" alt="SpringCloudfunction"></a>
  <a><img src="https://img.shields.io/badge/Spring%20webflux-%202.3.3.RELEASE-brightgreen.svg" alt="SpringBoot"></a>
  <a><img src="https://img.shields.io/badge/spring%20boot%20starter%20data%20r2dbc%20-%202.3.3.RELEASE-brightgreen.svg" alt="SpringBoot"></a>
  <a><img src="https://img.shields.io/badge/reactor%20R2dbc%20common%20core%20-%201.0.0-brightgreen.svg" alt="SpringBoot"></a>

<a><img src="https://img.shields.io/badge/reactor%20R2dbc%20orm%20-%201.0.0-brightgreen.svg" alt="SpringBoot"></a>

    
  <a><img src="https://img.shields.io/badge/Redis-orange.svg"></a>
  <a><img src="https://img.shields.io/badge/RabbitMq-orange.svg"></a>
  <a target="_blank" href="https://www.iviewui.com/docs/guide/install"><img src="https://img.shields.io/badge/iview-brightgreen.svg?style=flat-square" alt="iview"></a>
  <a><img src="https://img.shields.io/badge/vue-brightgreen.svg?style=flat-square" alt="vue"></a>
  <a><img src="https://img.shields.io/npm/l/express.svg" alt="License"></a>
  <a><img src="https://img.shields.io/badge/r2dbc-orange.svg"></a>
  <a><img src="https://img.shields.io/badge/springWebflux-orange.svg"></a>
  <a><img src="https://img.shields.io/badge/springCloudFunction-orange.svg"></a>
  <a><img src="https://img.shields.io/badge/springboot-orange.svg"></a>
  <a><img src="https://img.shields.io/badge/springCloudAlibaba-orange.svg"></a>





</p>  

## SpringCloud-webflux微服务平台 3.2.0-REALSE 


#### 简介
搭建基于OAuth2的开放平台、为APP端、应用服务提供统一接口管控平台、为第三方合作伙伴的业务对接提供授信可控的技术对接平台
+ 分布式架构，Nacos(服务注册+配置中心)统一管理
+ 统一API网关（参数验签、身份认证、接口鉴权、接口调试、接口限流、接口状态、接口外网访问）
+ 统一oauth2认证协议
+ 统一采用webflux+r2dbc+spring-cloud-function进行代码开发

#### 介绍
此项目是结合webflux+R2dbc+springcloudfunction实现权限/网关/动态路由等

#### 软件架构
1. [Spring Boot 2.3.x](https://spring.io/projects/spring-boot) springboot
2. [Spring WebFlux](https://spring.io/) 响应式Web支持
3. [R2DBC](https://r2dbc.io/) 响应式关系型数据库驱动
4. [Project Reactor](https://projectreactor.io/) 响应式编程框架
4. [Netty](https://netty.io/) ,[Vert.x](https://vertx.io/) 高性能网络编程框架
5. [ElasticSearch](https://www.elastic.co/cn/products/enterprise-search) 全文检索，日志，时序数据存储
6. [mysql](https://www.mysql.org) 业务功能数据管理
7. [spring cloud](https://spring.io/projects/spring-cloud)  微服务框架
8. [spring cloud alibaba](https://spring.io/projects/spring-cloud-alibaba) 阿里巴巴全家桶
9. [spring cloud function](https://spring.io/projects/spring-cloud-function) 函数-Function，消费者-Consumer和供应商-Supplier类型的@Beans的包装器，使用RabbitMQ，Kafka等将它们作为HTTP端点和/或消息流监听器/发布者公开给外部世界

#### 代码结构
```lua
xiguaapp-reactor
├── docs                               -- 文档及脚本
    ├── sql                            -- sql文件  
    ├── readme                         -- 介绍   
    ├── 权限架构图                       -- 权限架构图
      ├── data                         -- 增量数据
     
├── reactor-common                     -- 公共组件
    ├── common-dependencies            -- 依赖包 公用依赖包
    ├── common-api                     -- api文件 实体 feign接口
    ├── common-datasource              -- 动态数据源
    ├── common-core                    -- 核心公共组件(完善中...)
    ├── common-gateway                 -- 网关配置项(完善中...)
    ├── common-oauth                   -- 授权验证
    ├── common-r2dbc-orm               -- r2dbc封装orm基本配置类
    ├── common-server                  -- 服务工具类
    ├── common-starter                 -- 启动配置项(完善中...)
    ├── common-tenant                  -- 多租户(完善中...)
    ├── common-pay                     -- 支付模块
    ├── common-reactive-cache          -- 响应式流缓存工具类
    ├── common-tenant  
├── reactor-gateway                    -- 网关服务
├── reactor-open                       -- 开放平台jdk
├── reactor_platform                   -- 平台服务
    ├── reactor-bpm                    -- 工作流[port = 9000](推荐）  
    ├── reactor-job                    -- 分布式定时任务[port = 8888](功能完善）
    ├── reactor-msg                    -- 消息服务接口
    ├── ............................   -- 后期多重服务
├── reactor-sba                        -- 监控[port=4344]
├── reactor-service                    -- 基础服务
    ├── datagrap                       -- 代码生成、大屏数据、数据中台
    ├── report                         -- 报表
    ├── system                         -- 系统服务[port=8233]
    ├── ........                       -- 后续补充新的服务
    
├── reactor-simple                     -- 小弟研究r2dbc的demo
├── reactor-website                    -- 文档中心
    
```
#### 快速开始
本项目基于springCloud打造的分布式快速开发框架. 需要了解SpringCloud,SpringBoot,SpringSecurity,springCloudAlibaba,springCloudFunction,springWebflux,R2dbc,分布式原理
1.  准备环境
    + Java1.8  (v1.8.0_131+)
        + Nacos服务注册和配置中心(v1.0.0+) <a href="https://nacos.io/zh-cn/">阿里巴巴nacos.io</a>
        + Redis (v3.2.00+)
        + RabbitMq (v3.7+)（需安装rabbitmq_delayed_message_exchange插件 <a href="https://www.rabbitmq.com/community-plugins.html" target="_blank">下载地址</a>）
        + Mysql (v5.5.28+)
        + Maven (v3+)
        + Nodejs (v10.14.2+)
2.  执行创建数据库open-platform并执行sql脚本
    + doc/sql
    ......
3.  启动nacos服务发现&配置中心,新建公共配置文件 
    + 访问 http://localhost:8848/nacos/index.html 
    + 导入配置 /docs/config/DEFAULT_GROUP.zip（nacos1.0.3以上版本支持一键导入）
    + 新建配置文件  （nacos1.0.3以下版本）
        + 项目目录/docs/config/db.properties >  db.properties
        + 项目目录/docs/config/rabbitmq.properties > rabbitmq.properties
        + 项目目录/docs/config/redis.properties > redis.properties
        + 项目目录/docs/config/common.properties  > common.properties
          
     如图:
![img.png](img.png)
4.  修改主pom.xml  
    
        初始化maven项目
        ``` bush
            maven clean install
        ```
        本地启动,默认不用修改
        ``` xml
            <!--Nacos配置中心地址-->
            <config.server-addr>127.0.0.1:8848</config.server-addr>
            <!--Nacos配置中心命名空间,用于支持多环境.这里必须使用ID，不能使用名称,默认为空-->
            <config.namespace></config.namespace>
            <!--Nacos服务发现地址-->
            <discovery.server-addr>127.0.0.1:8848</discovery.server-addr>
        ```
        
5. 本地启动(按顺序启动)
     1. [必需]BaseApplication(平台基础服务)
     2. [必需]UaaAdminApplication(平台用户认证服务器)
     3. [必需]GatewaySpringApplication(推荐)
     ```
        访问 http://localhost:8888
     ```
     4.[非必需]SpringBootAdmin(监控服务器)(非必需)
      ```
          访问 http://localhost:8849
      ```
          
6. 前端启动
    ```bush
        npm install 
        npm run dev
    ``` 
    访问 http://localhost:8080
        
7. 项目打包部署  
    +  maven多环境打包,替换变量
   ```bush
     mvn clean install package -P {dev|test|online}
   ```

[comment]: <> (    + 项目启动)

[comment]: <> (    ```bush)

[comment]: <> (    ./docs/bin/startup.sh {start|stop|restart|status} open-cloud-base-server.jar)

[comment]: <> (    ./docs/bin/startup.sh {start|stop|restart|status} open-cloud-uaa-admin-server.jar)

[comment]: <> (    ./docs/bin/startup.sh {start|stop|restart|status} open-cloud-api-spring-server.jar)

[comment]: <> (    ```)
    
8. docker部署   
 +  配置DOCKER私服仓库
 +  maven多环境打包,替换变量.并构建docker镜像
   
[comment]: <> (       ```bush)

[comment]: <> (          clean install package -P {dev|test|online} dockerfile:build )

[comment]: <> (       ```  )


#### 模块提交说明
    此项目停止维护 作者已经被开除 出了bug自己解决



