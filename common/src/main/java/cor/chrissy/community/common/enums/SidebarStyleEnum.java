package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 侧边栏栏目枚举
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public enum SidebarStyleEnum {

    NOTICE(1),
    ARTICLES(2),
    RECOMMEND(3),
    ABOUT(4),
    COLUMN(5),
    PDF(6),
    SUBSCRIBE(7),
    ;

    private final int style;

    SidebarStyleEnum(int style) {
        this.style = style;
    }
}
