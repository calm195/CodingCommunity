package cor.chrissy.community.core.util;

import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.exception.CommunityException;

/**
 * 异常工具类
 *
 * @author wx128
 * @createAt 2024/12/17
 */
public class ExceptionUtil {
    public static CommunityException of(StatusEnum statusEnum, Object... args) {
        return new CommunityException(statusEnum, args);
    }
}
