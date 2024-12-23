package cor.chrissy.community.service.statistics.dto;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class StatisticsCountDTO {

    /**
     * PV 数量
     */
    private Integer pvCount;

    /**
     * 总用户数
     */
    private Integer userCount;

    /**
     * 文章数量
     */
    private Integer articleCount;
}

