package cor.chrissy.community.common.vo;

import lombok.Data;

/**
 * todo: 抽离？
 *
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class FlagBitDTO {

    /**
     * 标记位
     */
    private Integer flagBit;

    /**
     * 是否正向操作
     */
    private Boolean forward;


    public FlagBitDTO(Integer flagBit, Boolean forward) {
        this.flagBit =  flagBit;
        this.forward = forward;
    }
}
