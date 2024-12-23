package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 文章标签状态
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public enum FlagBitEnum {

    OFFICIAL(1, "官方"), // 1 > 0
    TOPPING(2, "置顶"), // 1 >> 1
    CREAM(4, "加精"); // 1 >> 2

    FlagBitEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static FlagBitEnum fromCode(Integer code) {
        for (FlagBitEnum value : FlagBitEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return FlagBitEnum.OFFICIAL;
    }
}

