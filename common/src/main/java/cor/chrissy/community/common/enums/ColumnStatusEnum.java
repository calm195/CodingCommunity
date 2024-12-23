package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 发布状态枚举
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public enum ColumnStatusEnum {

    OFFLINE(0, "未发布"),
    CONTINUE(1, "连载"),
    OVER(2, "已完结");

    ColumnStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final int code;
    private final String desc;

    public static ColumnStatusEnum fromCode(int code) {
        for (ColumnStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return ColumnStatusEnum.OFFLINE;
    }
}
