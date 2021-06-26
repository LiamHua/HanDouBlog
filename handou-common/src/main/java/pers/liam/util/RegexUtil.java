package pers.liam.util;

import java.util.regex.Pattern;

/**
 * 格式校验工具类
 * @author liam
 * @date 2021/4/15 23:54
 */
public class RegexUtil {
    /**
     * 手机号正则表达式
     */
    private static final String TEL_REGEX = "^[1][3,4,5,7,8][0-9]{9}$";

    /**
     * 手机号验证
     * @param tel 手机号
     * @return 是否手机号
     */
    public static boolean isTel(String tel) {
        Pattern pattern = Pattern.compile(TEL_REGEX);
        return pattern.matcher(tel).matches();
    }
}
