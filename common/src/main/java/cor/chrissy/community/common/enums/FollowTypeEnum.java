package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 用户关系状态枚举
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Getter
public enum FollowTypeEnum {

    FOLLOW("follow", "我关注的用户"),
    FANS("fans", "关注我的粉丝"),
    ;

    FollowTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final String code;
    private final String desc;

    public static FollowTypeEnum fromCode(String code) {
        for (FollowTypeEnum value : FollowTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return FollowTypeEnum.FOLLOW;
    }
}

