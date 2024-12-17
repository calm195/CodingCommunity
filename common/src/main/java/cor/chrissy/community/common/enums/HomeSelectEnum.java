package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 用户主页选项卡选项枚举
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Getter
public enum HomeSelectEnum {

    ARTICLE("article", "文章"),
    READ("read", "浏览记录"),
    FOLLOW("follow", "关注"),
    COLLECTION("collection", "收藏");

    HomeSelectEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // TODO: code 是否能优化
    private final String code;
    private final String desc;

    public static HomeSelectEnum formCode(String code) {
        for (HomeSelectEnum value : HomeSelectEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return HomeSelectEnum.ARTICLE;
    }
}
