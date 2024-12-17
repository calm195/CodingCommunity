package cor.chrissy.community.core.util;

/**
 * 数字工具类，TODO：math包里有无？
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public class NumUtil {

    public static boolean nullOrZero(Long num) {
        return num == null || num == 0L;
    }

    public static boolean nullOrZero(Integer num) {
        return num == null || num == 0;
    }

    public static boolean upZero(Long num) {
        return num != null && num > 0;
    }

    public static boolean upZero(Integer num) {
        return num != null && num > 0;
    }
}
