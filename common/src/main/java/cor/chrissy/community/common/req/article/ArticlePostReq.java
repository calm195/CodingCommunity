package cor.chrissy.community.common.req.article;

import cor.chrissy.community.common.enums.PushStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Data
public class ArticlePostReq implements Serializable {
    /**
     * 文章ID， 当存在时，表示更新文章
     */
    private Long articleId;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章短标题
     */
    private String subTitle;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 标签
     */
    private Set<Long> tagIds;

    /**
     * 简介
     */
    private String summery;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 封面
     */
    private String cover;

    /**
     * 文本类型
     *
     * @see cor.chrissy.community.common.enums.ArticleTypeEnum
     */
    private String articleType;


    /**
     * 来源：1-转载，2-原创，3-翻译
     *
     * @see cor.chrissy.community.common.enums.SourceTypeEnum
     */
    private Integer source;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * POST 发表, SAVE 暂存 DELETE 删除
     */
    private String actionType;

    public PushStatusEnum pushStatus() {
        if ("post".equalsIgnoreCase(actionType)) {
            return PushStatusEnum.ONLINE;
        } else {
            return PushStatusEnum.OFFLINE;
        }
    }

    public boolean deleted() {
        return "delete".equalsIgnoreCase(actionType);
    }
}
