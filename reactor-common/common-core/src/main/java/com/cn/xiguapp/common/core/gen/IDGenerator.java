package com.cn.xiguapp.common.core.gen;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiguaapp
 * @desc id生成器 用于生成id
 * @since 1.0 22:39
 */
@FunctionalInterface
public interface IDGenerator<T> {
    T generate();

    /**
     * 空ID生成器
     */
    IDGenerator<?> NULL = () -> null;

    @SuppressWarnings("unchecked")
    static <T> IDGenerator<T> getNullGenerator() {
        return (IDGenerator) NULL;
    }

    /**
     * 使用UUID生成id
     */
    IDGenerator<String> UUID = () -> java.util.UUID.randomUUID().toString();

    /**
     * 随机字符
     */
    IDGenerator<String> RANDOM = RandomUtil::randomChar;

    /**
     * md5(uuid()+random())
     */
    IDGenerator<String> MD5 = () -> {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(UUID.generate().concat(RandomUtil.randomChar()).getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * 雪花算法
     */
    IDGenerator<Long> SNOW_FLAKE = SnowflakeIdGenerator.getInstance()::nextId;

    /**
     * 雪花算法转String
     */
    IDGenerator<String> SNOW_FLAKE_STRING = () -> String.valueOf(SNOW_FLAKE.generate());

    /**
     * 雪花算法的16进制
     */
    IDGenerator<String> SNOW_FLAKE_HEX = () -> Long.toHexString(SNOW_FLAKE.generate());
}
