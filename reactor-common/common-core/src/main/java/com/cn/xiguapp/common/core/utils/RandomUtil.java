package com.cn.xiguapp.common.core.utils;

import java.util.Random;

/**
 * @author xiguaapp
 */
public class RandomUtil {

    /**
     * 创建密码
     *
     * @return 返回8位字母+数组密码
     */
    public static String createRandom() {
        return new Password().getRandomPassword();
    }

    /**
     * 只随机数字
     *
     * @return
     */
    public static String randomNumber() {
        return new Password(Password.StrengthLevel.ONLY_NUMBER, 10).getRandomPassword();
    }

    /**
     * 创建密码，指定强度和长度
     *
     * @param strengthLevel 强度等级
     * @param length        长度
     * @return 返回密码
     */
    public static String createRandom(Password.StrengthLevel strengthLevel, int length) {
        return new Password(strengthLevel, length).getRandomPassword();
    }

    public static class Password {


        public Password() {
            this(StrengthLevel.NUMBER_LOWERCASE, 8);
        }

        /**
         * @param strengthLevel 强度等级
         * @param length        长度
         */
        public Password(StrengthLevel strengthLevel, int length) {
            if (length <= 0) {
                throw new IllegalArgumentException("length can not <= 0");
            }
            this.strengthLevel = strengthLevel;
            this.length = length;
        }

        /**
         * 强度等级
         */
        private final StrengthLevel strengthLevel;

        /**
         * 需要的密码长度
         */
        private final int length;

        private static final Random RANDOM = new Random();

        /**
         * 因为伪随机数random在nextInt()的时候会出现向更小的数偏离的情况，所以用一个randomMax来修正
         */
        private static final int RANDOM_MAX = 100000000;
        /**
         * 因为数组下标从0开始
         */
        private static final int INDEX = 1;
        /**
         * 一共26个字母
         */
        private static final int NUMBER_OF_LETTER = 26;
        /**
         * ascii码表数字从48开始
         */
        private static final int NUMBER_INDEX = 48;
        /**
         * 数组下标0-9代表10个数字
         */
        private static final int NUMBER_MAX = 9;
        /**
         * 大写字母ascii码表从65开始
         */
        private static final int CAPITAL_INDEX = 65;
        /**
         * 小写字母ascii码表从65开始
         */
        private static final int LOWERCASE_INDEX = 97;
        /**
         * 特殊字符的起始ascii码表序号取第一个数字
         */
        private static final int SPECIAL = 0;
        /**
         * 特殊字符集，第一个数字代表ascii码表序号，第二个代表从这里开始一共使用多少个字符
         */
        private static final int[][] SPECIAL_CHARACTER = {{33, 14}, {58, 6}, {91, 5}, {123, 3}};

        /**
         * 取随机密码
         *
         * @return 返回随机密码
         */
        public String getRandomPassword() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append((char) getNextChar());
            }
            return builder.toString();
        }

        /**
         * 取得下一个ascii编码
         *
         * @return 返回ascii码
         */
        private int getNextChar() {
            // ascii编码
            int x = 0;
            // 伪字符ascii编码
            final int puppetLetter = RANDOM.nextInt(RANDOM_MAX) % NUMBER_OF_LETTER;
            // 伪数字ascii编码
            final int puppetNumber = RANDOM.nextInt(RANDOM_MAX) % NUMBER_MAX + NUMBER_INDEX;
            // 按照字符等级强度取ascii值
            final int levelIndex = RANDOM.nextInt(RANDOM_MAX) % strengthLevel.level;
            // 特殊字符的随机集合下标（数组第一维）
            final int specialType = RANDOM.nextInt(RANDOM_MAX) % SPECIAL_CHARACTER.length;
            // 特殊字符的ascii编码
            final int specialInt = RANDOM.nextInt(RANDOM_MAX) % SPECIAL_CHARACTER[specialType][INDEX] + SPECIAL_CHARACTER[specialType][SPECIAL];
            // 根据密码强度等级获取随机ascii编码
            switch (strengthLevel) {
                case ONLY_NUMBER:
                    x = puppetNumber;
                    break;
                case NUMBER_LOWERCASE:
                    x = levelIndex == INDEX ? puppetNumber : puppetLetter + LOWERCASE_INDEX;
                    break;
                case NUMBER_LOWERCASE_CAPITAL:
                    if (levelIndex == 0) {
                        x = puppetNumber;
                    } else if (levelIndex == INDEX) {
                        x = puppetLetter + LOWERCASE_INDEX;
                    } else {
                        x = puppetLetter + CAPITAL_INDEX;
                    }
                    break;
                case NUMBER_LOWERCASE_CAPITAL_CHARACTER:
                    if (levelIndex == 0) {
                        x = puppetNumber;
                    } else if (levelIndex == INDEX) {
                        x = puppetLetter + LOWERCASE_INDEX;
                    } else if (levelIndex == INDEX * 2) {
                        x = puppetLetter + CAPITAL_INDEX;
                    } else {
                        x = specialInt;
                    }
                    break;
                default:
            }
            return x;
        }

        /**
         * Password level config
         * 1 level : only number
         * 2 level : number and lowercase letters
         * 3 level : number , lowercase letters , capital letters
         * 4 level : number , lowercase letters , capital letters , special characters
         */
        public enum StrengthLevel {
            ONLY_NUMBER(1),
            NUMBER_LOWERCASE(2),
            NUMBER_LOWERCASE_CAPITAL(3),
            NUMBER_LOWERCASE_CAPITAL_CHARACTER(4),
            ;

            StrengthLevel(int level) {
                this.level = level;
            }

            private int level;

        }
    }

}
