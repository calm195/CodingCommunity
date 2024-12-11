package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Getter
public enum PushStatEnum {

    OFFLINE(0, "未发布"),
    ONLINE(1,"已发布");

    PushStatEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final int code;
    private final String desc;

    public static PushStatEnum formCode(int code) {
        for (PushStatEnum yesOrNoEnum : PushStatEnum.values()) {
            if (yesOrNoEnum.getCode() == code) {
                return yesOrNoEnum;
            }
        }
        return PushStatEnum.OFFLINE;
    }
}
