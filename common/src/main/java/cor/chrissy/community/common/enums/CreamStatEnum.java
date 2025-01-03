package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 文章加精状态
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public enum CreamStatEnum {

    NOT_CREAM(0, "不加精"),
    CREAM(1, "加精"),
    ;

    CreamStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static CreamStatEnum fromCode(Integer code) {
        for (CreamStatEnum value : CreamStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CreamStatEnum.NOT_CREAM;
    }
}

