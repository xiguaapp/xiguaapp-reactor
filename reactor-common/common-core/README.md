#此项为整个项目的核心工具包 包含表达式解析 响应式excel倒入导出 id生成 时间处理工具类 签名工具类 Stream优化工具 自定义线程池 响应式对象复制 异常等工具类
[![Build Status](https://travis-ci.com/hs-web/reactor-excel.svg?branch=master)](https://travis-ci.com/hs-web/reactor-excel)
[![codecov](https://codecov.io/gh/hs-web/reactor-excel/branch/master/graph/badge.svg)](https://codecov.io/gh/hs-web/reactor-excel)
# 基于Reactor的excel,csv导入导出

##响应式excel写操作
```java
ReactorExcel
        .writer("csv")
        .header("id", "ID")
        .header("name", "name")
        .write(Flux.range(0, 1000)
                .map(i -> new HashMap<String, Object>() {{
                    put("id", i);
                    put("name", "test" + i);
                }}), new FileOutputStream("./target/test.csv"))
        .as(StepVerifier::create)
        .expectComplete()
        .verify();

```
##响应式读操作
```java

 ReactorExcel
        .mapReader("csv")
        .read(inputStream)
        .as(StepVerifier::create)
        .subscribe(map->System.out.println(map));

```