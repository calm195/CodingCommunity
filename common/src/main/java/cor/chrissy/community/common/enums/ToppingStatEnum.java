package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 是否置顶
 *
 * @author wx128
 * @createAt 2024/12/23
 */
@Getter
public enum ToppingStatEnum {

    NOT_TOPPING(0, "不置顶"),
    TOPPING(1, "置顶"),
    ;

    ToppingStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ToppingStatEnum fromCode(Integer code) {
        for (ToppingStatEnum value : ToppingStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ToppingStatEnum.NOT_TOPPING;
    }
}
