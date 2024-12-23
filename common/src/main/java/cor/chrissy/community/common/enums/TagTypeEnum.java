package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 标签类型枚举
 *
 * @author wx128
 * @createAt 2024/12/9
 */
@Getter
public enum TagTypeEnum {

    SYSTEM_TAG(1, "系统标签"),
    CUSTOM_TAG(2, "已读");

    TagTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static TagTypeEnum fromCode(Integer code) {
        for (TagTypeEnum value : TagTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return TagTypeEnum.SYSTEM_TAG;
    }
}
