package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 用户操作类型枚举
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Getter
public enum OperateTypeEnum {

    EMPTY(0, ""),
    READ(1, "阅读"),
    PRAISE(2, "点赞"),
    COLLECTION(3, "收藏"),
    CANCEL_PRAISE(4, "取消点赞"),
    CANCEL_COLLECTION(5, "取消收藏"),
    COMMENT(6, "评论"),
    DELETE_COMMENT(7, "删除评论"),
    ;

    OperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static OperateTypeEnum fromCode(Integer code) {
        for (OperateTypeEnum value : OperateTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OperateTypeEnum.EMPTY;
    }
}
