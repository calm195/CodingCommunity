package cor.chrissy.community.core.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 验证码生成器，目前：三位给定数字；todo：随机产生并缓存
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public class CodeGenerateUtil {
    public static final Integer CODE_LEN = 3;

    private static final Random random = new Random();

    private static final List<String> specialCodes = Arrays.asList(
            "666", "888", "000", "999", "555", "222", "333", "777",
            "520", "234", "345", "456", "567", "678", "789"
    );

    public static String genCode(int cnt) {
        if (cnt >= specialCodes.size()) {
            int num = random.nextInt(1000);
            if (num >= 100 && num <= 200) {
                // 100-200之间的数字作为关键词回复，不用于验证码
                return genCode(cnt);
            }
            return String.format("%0" + CODE_LEN + "d", num);
        } else {
            return specialCodes.get(cnt);
        }
    }

    public static boolean isVerifyCode(String content) {
        if (!NumberUtils.isDigits(content) || content.length() != CodeGenerateUtil.CODE_LEN) {
            return false;
        }

        int num = Integer.parseInt(content);
        return num < 100 || num > 200;
    }
}
