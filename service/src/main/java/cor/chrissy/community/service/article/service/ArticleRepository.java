package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;

import java.util.List;
import java.util.Set;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
public interface ArticleRepository {
    /**
     * 查询文章详情
     *
     * @param articleId
     * @return
     */
    ArticleDTO queryArticleDetail(Long articleId);

    /**
     * 保存or更新文章
     *
     * @param article
     * @param content
     * @param tags
     * @return
     */
    Long saveArticle(ArticleDO article, String content, Set<Long> tags);

    /**
     * 分页获取用户的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<ArticleDO> getArticleListByUserId(Long userId, PageParam pageParam);

    /**
     * 分页获取文章列表
     *
     * @param categoryId
     * @param pageParam
     * @return
     */
    List<ArticleDO> getArticleListByCategoryId(Long categoryId, PageParam pageParam);
}

