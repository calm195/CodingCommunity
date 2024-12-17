package cor.chrissy.community.core.util;

import java.util.Random;

/**
 * 验证码生成器，目前：Random(1000_000)
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public class CodeGenerateUtil {

    private static final Random random = new Random();

    public static String genCode() {
        return String.format("%06d", random.nextInt(1000_000));
    }
}
