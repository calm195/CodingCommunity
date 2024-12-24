package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 文本信息类型枚举
 *
 * @author wx128
 * @createAt 2024/12/9
 */
@Getter
public enum DocumentTypeEnum {

    EMPTY(0, ""),
    ARTICLE(1, "文章"),
    COMMENT(2, "评论"),
    ;

    DocumentTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static DocumentTypeEnum fromCode(Integer code) {
        for (DocumentTypeEnum value : DocumentTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return DocumentTypeEnum.EMPTY;
    }
}
