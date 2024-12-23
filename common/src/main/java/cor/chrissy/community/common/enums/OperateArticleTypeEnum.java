package cor.chrissy.community.common.enums;

import cor.chrissy.community.common.vo.FlagBitDTO;
import lombok.Getter;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Getter
public enum OperateArticleTypeEnum {

    OFFICIAL(1, "官方"){
        @Override
        public FlagBitDTO getFlagBit() {
            return new FlagBitDTO(FlagBitEnum.OFFICIAL.getCode(), Boolean.TRUE);
        }
    },
    CANCEL_OFFICIAL(2, "取消官方"){
        @Override
        public FlagBitDTO getFlagBit() {
            return new FlagBitDTO(FlagBitEnum.OFFICIAL.getCode(), Boolean.FALSE);
        }
    },
    TOPPING(3, "置顶"){
        @Override
        public FlagBitDTO getFlagBit() {
            return new FlagBitDTO(FlagBitEnum.TOPPING.getCode(), Boolean.TRUE);
        }
    },
    CANCEL_TOPPING(4, "取消置顶"){
        @Override
        public FlagBitDTO getFlagBit() {
            return new FlagBitDTO(FlagBitEnum.TOPPING.getCode(), Boolean.FALSE);
        }
    },
    CREAM(5, "加精"){
        @Override
        public FlagBitDTO getFlagBit() {
            return new FlagBitDTO(FlagBitEnum.CREAM.getCode(), Boolean.TRUE);
        }
    },
    CANCEL_CREAM(6, "取消加精"){
        @Override
        public FlagBitDTO getFlagBit() {
            return new FlagBitDTO(FlagBitEnum.CREAM.getCode(), Boolean.FALSE);
        }
    };

    OperateArticleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static OperateArticleTypeEnum fromCode(Integer code) {
        for (OperateArticleTypeEnum value : OperateArticleTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OperateArticleTypeEnum.OFFICIAL;
    }

    public abstract FlagBitDTO getFlagBit();
}

