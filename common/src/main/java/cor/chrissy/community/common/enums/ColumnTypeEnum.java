package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * @author wx128
 * @createAt 2024/12/26
 */
@Getter
public enum ColumnTypeEnum {

    FREE(0, "免费"),
    LOGIN(1, "登录阅读"),
    TIME_FREE(2, "限时免费");

    ColumnTypeEnum(int code, String desc) {
        this.type = code;
        this.desc = desc;
    }

    private final int type;
    private final String desc;

    // todo rename
    public static ColumnTypeEnum formCode(int code) {
        for (ColumnTypeEnum status : values()) {
            if (status.getType() == code) {
                return status;
            }
        }
        return ColumnTypeEnum.FREE;
    }
}