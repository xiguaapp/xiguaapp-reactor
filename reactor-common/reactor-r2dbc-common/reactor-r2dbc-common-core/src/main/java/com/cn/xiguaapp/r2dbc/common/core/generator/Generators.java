package com.cn.xiguaapp.r2dbc.common.core.generator;

/**
 * @author xiguaapp
 * @desc 加密描述
 * @since 1.0 22:13
 */
public interface Generators {
    /**
     * @see DefaultIdGenerator
     */
    String DEFAULT_ID_GENERATOR = "default_id";


    /**
     * @see MD5Generator
     */
    String MD5 = "md5";

    /**
     * @see SnowFlakeStringIdGenerator
     */
    String SNOW_FLAKE = "snow_flake";

    /**
     * @see CurrentTimeGenerator
     */
    String CURRENT_TIME = "current_time";
}
