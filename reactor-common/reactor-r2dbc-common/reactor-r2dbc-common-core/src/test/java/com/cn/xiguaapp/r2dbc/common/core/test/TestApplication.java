package com.cn.xiguaapp.r2dbc.common.core.test;

import com.cn.xiguaapp.r2dbc.common.api.crud.entity.EntityFactory;
import com.cn.xiguaapp.r2dbc.common.core.entity.factory.MapperEntityFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Configuration
public class TestApplication {

    @Bean
    public EntityFactory entityFactory(){
        return new MapperEntityFactory();
    }
}
