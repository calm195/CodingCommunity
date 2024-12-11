package cor.chrissy.community.service.article.dto;

import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class ArticleListDTO {

    /**
     * 文章列表
     */
    List<ArticleDTO> articleList;

    /**
     * 是否有更多
     */
    private Boolean isMore;
}

