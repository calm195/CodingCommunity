package cor.chrissy.community.service.article.dto;

import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import lombok.Data;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class ColumnArticlesDTO {
    /**
     * 专栏详情
     */
    private Long column;

    /**
     * 文章详情
     */
    private ArticleDTO article;

    /**
     * 文章评论
     */
    private List<TopCommentDTO> comments;

    /**
     * 热门评论
     */
    private TopCommentDTO hotComment;

    /**
     * 文章目录列表
     */
    private List<SimpleArticleDTO> articleList;
}

