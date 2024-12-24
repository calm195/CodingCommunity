package cor.chrissy.community.web.front.article.vo;

import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@Data
public class ArticleListVo {
    /**
     * 归档类型
     */
    private String archives;
    /**
     * 归档id
     */
    private Long archiveId;

    private PageListVo<ArticleDTO> articles;
}

