package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Getter
public enum DocumentTypeEnum {

    EMPTY(0, ""),
    DOCUMENT(1, "文章"),
    COMMENT(2, "评论");

    DocumentTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static DocumentTypeEnum formCode(Integer code) {
        for (DocumentTypeEnum value : DocumentTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return DocumentTypeEnum.EMPTY;
    }
}