package com.cn.xiguapp.common.core.utils;

import cn.hutool.core.util.StrUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author xiguaapp
 * @Date 2020/9/24
 * @desc 字符串工具类
 */
public class StringUtils extends StrUtil {
    private static final char SEPARATOR = '_';

    /**
     * 驼峰转下划线
     * createTime > create_time
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(SEPARATOR);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 下划线转驼峰
     * create_time > createTime
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile(String.valueOf(SEPARATOR)).matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 格式化存储单位
     *
     * @param size byte 字节
     * @return
     */
    public static String formatBytes(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        int bytes = 1024;
        if (size < bytes) {
            return String.valueOf(size) + "Byte";
        } else {
            size = size / bytes;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位 //因为还没有到达要使用另一个单位的时候 //接下去以此类推
        if (size < bytes) {
            return String.valueOf(size) + "K";
        } else {
            size = size / bytes;
        }
        if (size < bytes) {
            // 因为如果以MB为单位的话，要保留最后1位小数， //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "M";
        } else { // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / bytes;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "G";
        }
    }

    /**
     * 匿名手机号
     *
     * @param mobile
     * @return 152****4799
     */
    public static String formatMobile(String mobile) {
        if (isEmpty(mobile)) {
            return null;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 匿名银行卡号
     *
     * @param bankCard
     * @return
     */
    public static String formatBankCard(String bankCard) {
        if (isEmpty(bankCard)) {
            return null;
        }
        return bankCard.replaceAll("(\\d{5})\\d{5}\\d{2}(\\d{4})", "$1****$2");
    }

    /**
     * 匿名身份证
     *
     * @param idCard
     * @return 4304*****7733
     */
    public static String formatIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return null;
        }
        return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
    }

    /**
     * 检测是否未手机号
     * 中国电信号段
     * 133、149、153、173、177、180、181、189、199
     * 中国联通号段
     * 130、131、132、145、155、156、166、175、176、185、186
     * 中国移动号段
     * 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
     * 其他号段
     * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
     * 虚拟运营商
     * 电信：1700、1701、1702
     * 移动：1703、1705、1706
     * 联通：1704、1707、1708、1709、171
     *
     * @param mobile
     * @return
     */
    public static boolean matchMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 检测Email
     *
     * @param email
     * @return
     */
    public static boolean matchEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }


    /**
     * 检测域名
     *
     * @param domain
     * @return
     */
    public static boolean matchDomain(String domain) {
        if (domain == null) {
            return false;
        }
        String regex = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$";
        return Pattern.matches(regex, domain);
    }

    /**
     * 检测IP
     *
     * @param ip
     * @return
     */
    public static boolean matchIp(String ip) {
        if (ip == null) {
            return false;
        }
        String regex = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
        return Pattern.matches(regex, ip);
    }

    /**
     * 检测HttpUrl
     *
     * @param url
     * @return
     */
    public static boolean matchHttpUrl(String url) {
        if (url == null) {
            return false;
        }
        String regex = "^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*([\\?&]\\w+=\\w*)*$";
        return Pattern.matches(regex, url);
    }

    /**
     * 密码强度
     *
     * @return Z = 字母 S = 数字 T = 特殊字符
     */
    public static String checkPassword(String passwordStr) {
        String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
        String regexZT = "\\D*";
        String regexST = "[\\d\\W]*";
        String regexZS = "\\w*";
        String regexZST = "[\\w\\W]*";

        if (passwordStr.matches(regexZ)) {
            return "弱";
        }
        if (passwordStr.matches(regexS)) {
            return "弱";
        }
        if (passwordStr.matches(regexT)) {
            return "弱";
        }
        if (passwordStr.matches(regexZT)) {
            return "中";
        }
        if (passwordStr.matches(regexST)) {
            return "中";
        }
        if (passwordStr.matches(regexZS)) {
            return "中";
        }
        if (passwordStr.matches(regexZST)) {
            return "强";
        }
        return passwordStr;
    }


    /**
     * 将 Exception 转化为 String
     */
    public static String getExceptionToString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 把通用字符编码的字符串转化为汉字编码。
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (!isEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }
    /**
     * 过滤不可见字符
     */
    public static String stripNonValidXMLCharacters(String input) {
        if (input == null || ("".equals(input))) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        char current;
        for (int i = 0; i < input.length(); i++) {
            current = input.charAt(i);
            if ((current == 0x9) || (current == 0xA) || (current == 0xD)
                    || ((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD))
                    || ((current >= 0x10000) && (current <= 0x10FFFF))) {
                out.append(current);
            }
        }
        return out.toString();
    }
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : padding(pads, padChar).concat(str);
            }
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }

    private static String padding(int repeat, char padChar) {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        } else {
            char[] buf = new char[repeat];

            for(int i = 0; i < buf.length; ++i) {
                buf[i] = padChar;
            }

            return new String(buf);
        }
    }

    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     *
     * @param value 待检查的字符串
     * @return true/false
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查对象是否为数字型字符串,包含负数开头的。
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        char[] chars = obj.toString().toCharArray();
        int length = chars.length;
        if(length < 1) {
            return false;
        }

        int i = 0;
        if(length > 1 && chars[0] == '-') {
            i = 1;
        }

        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap();
    static final char CN_CHAR_START = '一';
    static final char CN_CHAR_END = '龥';

    public StringUtils() {
    }

    public static final Pattern compileRegex(String regex) {
        Pattern pattern = (Pattern)PATTERN_CACHE.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            PATTERN_CACHE.put(regex, pattern);
        }

        return pattern;
    }

    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }

    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }
    }

    public static final String underScoreCase2CamelCase(String str) {
        if (!str.contains("_")) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            char[] chars = str.toCharArray();
            boolean hitUnderScore = false;
            sb.append(chars[0]);

            for(int i = 1; i < chars.length; ++i) {
                char c = chars[i];
                if (c == '_') {
                    hitUnderScore = true;
                } else if (hitUnderScore) {
                    sb.append(Character.toUpperCase(c));
                    hitUnderScore = false;
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static final String camelCase2UnderScoreCase(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();

        for(int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String throwable2String(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static String concat(Object... more) {
        return concatSpiltWith("", more);
    }

    public static String concatSpiltWith(String split, Object... more) {
        StringBuilder buf = new StringBuilder();

        for(int i = 0; i < more.length; ++i) {
            if (i != 0) {
                buf.append(split);
            }

            buf.append(more[i]);
        }

        return buf.toString();
    }

    public static String toASCII(String str) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = str.getBytes();

        for(int i = 0; i < bGBK.length; ++i) {
            strBuf.append(Integer.toHexString(bGBK[i] & 255));
        }

        return strBuf.toString();
    }

    /**
     * 中文字符串转化为unicode
     * @param str
     * @return
     */
    public static String toUnicode(String str) {
        StringBuffer strBuf = new StringBuffer();
        char[] chars = str.toCharArray();

        for(int i = 0; i < chars.length; ++i) {
            strBuf.append("\\u").append(Integer.toHexString(chars[i]));
        }

        return strBuf.toString();
    }

    /**
     * unicode转化为中文字符串
     * @param chars
     * @return
     */
    public static String toUnicodeString(char[] chars) {
        StringBuffer strBuf = new StringBuffer();

        for(int i = 0; i < chars.length; ++i) {
            strBuf.append("\\u").append(Integer.toHexString(chars[i]));
        }

        return strBuf.toString();
    }

    public static boolean containsChineseChar(String str) {
        char[] chars = str.toCharArray();

        for(int i = 0; i < chars.length; ++i) {
            if (chars[i] >= 19968 && chars[i] <= '龥') {
                return true;
            }
        }

        return false;
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || "".equals(obj.toString());
    }

    public static boolean isNumber(Object obj) {
        if (obj instanceof Number) {
            return true;
        } else {
            return isInt(obj) || isDouble(obj);
        }
    }

    public static String matcherFirst(String patternStr, String text) {
        Pattern pattern = compileRegex(patternStr);
        Matcher matcher = pattern.matcher(text);
        String group = null;
        if (matcher.find()) {
            group = matcher.group();
        }

        return group;
    }

    public static boolean isInt(Object obj) {
        if (isNullOrEmpty(obj)) {
            return false;
        } else {
            return obj instanceof Integer ? true : obj.toString().matches("[-+]?\\d+");
        }
    }

    public static boolean isDouble(Object obj) {
        if (isNullOrEmpty(obj)) {
            return false;
        } else {
            return !(obj instanceof Double) && !(obj instanceof Float) ? compileRegex("[-+]?\\d+\\.\\d+").matcher(obj.toString()).matches() : true;
        }
    }

    public static boolean isBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return true;
        } else {
            String strVal = String.valueOf(obj);
            return "true".equalsIgnoreCase(strVal) || "false".equalsIgnoreCase(strVal);
        }
    }

    public static boolean isTrue(Object obj) {
        return "true".equals(String.valueOf(obj));
    }

    public static boolean contains(Object[] arr, Object... obj) {
        return arr != null && obj != null && arr.length != 0 ? Arrays.asList(arr).containsAll(Arrays.asList(obj)) : false;
    }

    public static int toInt(Object object, int defaultValue) {
        if (object instanceof Number) {
            return ((Number)object).intValue();
        } else if (isInt(object)) {
            return Integer.parseInt(object.toString());
        } else {
            return isDouble(object) ? (int)Double.parseDouble(object.toString()) : defaultValue;
        }
    }

    public static int toInt(Object object) {
        return toInt(object, 0);
    }

    public static long toLong(Object object, long defaultValue) {
        if (object instanceof Number) {
            return ((Number)object).longValue();
        } else if (isInt(object)) {
            return Long.parseLong(object.toString());
        } else {
            return isDouble(object) ? (long)Double.parseDouble(object.toString()) : defaultValue;
        }
    }

    public static long toLong(Object object) {
        return toLong(object, 0L);
    }

    public static double toDouble(Object object, double defaultValue) {
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        } else if (isNumber(object)) {
            return Double.parseDouble(object.toString());
        } else {
            return null == object ? defaultValue : 0.0D;
        }
    }

    public static double toDouble(Object object) {
        return toDouble(object, 0.0D);
    }

    public static String[] splitFirst(String str, String regex) {
        return str.split(regex, 2);
    }

    public static String toString(Object object) {
        return toString(object, (String)null);
    }

    public static String toString(Object object, String defaultValue) {
        return object == null ? defaultValue : String.valueOf(object);
    }

    /**
     * 删除存在字符并转换List
     * @param object 原始字符串
     * @param regex 需要删除的字符串
     * @return 字符数组
     */
    public static final String[] toStringAndSplit(Object object, String regex) {
        return isNullOrEmpty(object) ? null : String.valueOf(object).split(regex);
    }

    /**
     * 判断是否为中文
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0.0F;
        float count = 0.0F;

        for(int i = 0; i < ch.length; ++i) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    ++count;
                }

                ++chLength;
            }
        }

        float result = count / chLength;
        if ((double)result > 0.4D) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将数据转化为a:v:x格式
     * @param strings
     * @return
     */
    public static String assembledRedisKey(Object...strings){
        StringBuilder result=new StringBuilder("");
        if(strings!=null&&strings.length>0){
            for(int i =0;i<strings.length;i++){
                if(i==0){
                    result.append(strings[i]);
                }else{
                    result.append(":").append(strings[i]);
                }
            }
        }
        return result.toString();
    }
    /**
     * 如果给定字符串{@code str}中不包含{@code appendStr}，则在{@code str}后追加{@code appendStr}；
     * 如果已包含{@code appendStr}，则在{@code str}后追加{@code otherwise}
     *
     * @param str       给定的字符串
     * @param appendStr 需要追加的内容
     * @param otherwise 当{@code appendStr}不满足时追加到{@code str}后的内容
     * @return 追加后的字符串
     */
    public static String appendIfNotContain(String str, String appendStr, String otherwise) {
        if (isEmpty(str) || isEmpty(appendStr)) {
            return str;
        }
        if (str.contains(appendStr)) {
            return str.concat(otherwise);
        }
        return str.concat(appendStr);
    }

    /**
     * 对 subject body 进行 trim 运算，
     * 以防止在签名是可能造成的签名错误问题
     * @param str 字符
     * @return 去除空格之后的字符
     */
    public static String tryTrim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * @param content 需要加密串
     * @param charset 字符集
     * @return 加密后的字节数组
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("转码过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.matchDomain("515608851@qq.com"));
        System.out.println(StringUtils.matchDomain("www.qq.com"));
        System.out.println(StringUtils.matchDomain("qq.com"));
        System.out.println(StringUtils.matchIp("qq.com"));
        System.out.println(StringUtils.matchIp("192.168.0.1"));
        System.out.println(StringUtils.assembledRedisKey(1,"2","4"));
        System.out.println(StringUtils.toUnicode("傻逼"));
        System.out.println(StringUtils.formatMobile("15284531054"));
        System.out.println(StringUtils.checkPassword("521213.QQ.@aad"));
        System.out.println(matchHttpUrl("http://www.baidu.top"));
        System.out.println(formatBankCard("6228483868222541275"));
        System.out.println(Arrays.stream(toStringAndSplit("狗卫苏", "卫")).collect(Collectors.toList()));
    }

}
