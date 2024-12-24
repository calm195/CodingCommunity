package cor.chrissy.community.common.enums;

import lombok.Getter;

/**
 * 文章各种操作状态枚举
 *
 * @author wx128
 * @createAt 2024/12/23
 */
@Getter
public enum OperateArticleEnum {

    EMPTY(0, "") {
        @Override
        public int getDbStatCode() {
            return 0;
        }
    },
    OFFICIAL(1, "官方") {
        @Override
        public int getDbStatCode() {
            return OfficialStatEnum.OFFICIAL.getCode();
        }
    },
    CANCEL_OFFICIAL(2, "非官方") {
        @Override
        public int getDbStatCode() {
            return OfficialStatEnum.NOT_OFFICIAL.getCode();
        }
    },
    TOPPING(3, "置顶") {
        @Override
        public int getDbStatCode() {
            return ToppingStatEnum.TOPPING.getCode();
        }
    },
    CANCEL_TOPPING(4, "不置顶") {
        @Override
        public int getDbStatCode() {
            return ToppingStatEnum.NOT_TOPPING.getCode();
        }
    },
    CREAM(5, "加精") {
        @Override
        public int getDbStatCode() {
            return CreamStatEnum.CREAM.getCode();
        }
    },
    CANCEL_CREAM(6, "不加精") {
        @Override
        public int getDbStatCode() {
            return CreamStatEnum.NOT_CREAM.getCode();
        }
    },
    ;

    OperateArticleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static OperateArticleEnum fromCode(Integer code) {
        for (OperateArticleEnum value : OperateArticleEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OperateArticleEnum.OFFICIAL;
    }

    public abstract int getDbStatCode();
}
