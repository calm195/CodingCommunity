package cor.chrissy.community.service.article.dto;

import lombok.Data;

/**
 * @author wx128
 * @createAt 2024/12/17
 */
@Data
public class ColumnFootCountDTO {

    /**
     * 专栏点赞数
     */
    private Integer praiseCount;

    /**
     * 专栏被阅读数
     */
    private Integer readCount;

    /**
     * 专栏被收藏数
     */
    private Integer collectionCount;

    /**
     * 专栏评论数
     */
    private Integer commentCount;

    /**
     * 专栏文章数
     */
    private Integer articleCount;

    public ColumnFootCountDTO() {
        praiseCount = 0;
        readCount = 0;
        collectionCount = 0;
        commentCount = 0;
        articleCount = 0;
    }
}

