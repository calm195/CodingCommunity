package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * @author wx128
 * @createAt 2024/12/23
 */
@Getter
public enum OfficialStatEnum {

    NOT_OFFICIAL(0, "非官方"),
    OFFICIAL(1, "官方");

    OfficialStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static OfficialStatEnum formCode(Integer code) {
        for (OfficialStatEnum value : OfficialStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OfficialStatEnum.NOT_OFFICIAL;
    }
}
