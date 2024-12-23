package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 通知 - 文章阅读
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public enum NotifyStatEnum {
    UNREAD(0, "未读"),
    READ(1, "已读");


    private int stat;
    private String msg;

    NotifyStatEnum(int type, String msg) {
        this.stat = type;
        this.msg = msg;
    }
}
