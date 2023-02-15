package com.cn.xiguaapp.system.server.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 16:32
 */
@Data
@Configuration
@ConfigurationProperties(prefix = ReactorProperties.PREFIX,ignoreInvalidFields=true,ignoreUnknownFields=false)
public class ReactorProperties {
    public static final String PREFIX = "xiguaapp.reactor";
    private String signType;
    private String secret;

}
