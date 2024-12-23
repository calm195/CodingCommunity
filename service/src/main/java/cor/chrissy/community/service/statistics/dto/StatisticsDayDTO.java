package cor.chrissy.community.service.statistics.dto;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class StatisticsDayDTO {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer count;
}
