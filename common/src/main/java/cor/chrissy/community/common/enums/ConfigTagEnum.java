package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 文章标签枚举
 *
 * @author wx128
 * @createAt 2024/12/23
 */
@Getter
public enum ConfigTagEnum {

    EMPTY(0, ""),
    HOT(1, "热门"),
    OFFICIAL(2, "官方"),
    COMMENT(3, "推荐"),
    ;

    ConfigTagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ConfigTagEnum formCode(Integer code) {
        for (ConfigTagEnum value : ConfigTagEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ConfigTagEnum.EMPTY;
    }
}
