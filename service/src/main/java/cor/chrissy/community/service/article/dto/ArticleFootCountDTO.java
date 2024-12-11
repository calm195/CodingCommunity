package cor.chrissy.community.service.article.dto;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class ArticleFootCountDTO {

    /**
     * 文章点赞数
     */
    private Integer praiseCount;

    /**
     * 文章被阅读数
     */
    private Integer readCount;

    /**
     * 文章被收藏数
     */
    private Integer collectionCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    public ArticleFootCountDTO() {
        praiseCount = 0;
        readCount = 0;
        collectionCount = 0;
        commentCount = 0;
    }
}
