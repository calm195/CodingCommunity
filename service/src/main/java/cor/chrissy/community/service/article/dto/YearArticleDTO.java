package cor.chrissy.community.service.article.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
@ToString(callSuper = true)
public class YearArticleDTO {

    /**
     * 年份
     */
    private String year;

    /**
     * 文章数量
     */
    private Integer articleCount;
}
