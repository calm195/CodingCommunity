package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 选择转换界面的枚举
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Getter
public enum FollowSelectEnum {

    FOLLOW("follow", "我关注的用户"),
    FANS("fans", "关注我的粉丝"),
    ;

    FollowSelectEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final String code;
    private final String desc;

    public static FollowSelectEnum fromCode(String code) {
        for (FollowSelectEnum value : FollowSelectEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return FollowSelectEnum.FOLLOW;
    }
}