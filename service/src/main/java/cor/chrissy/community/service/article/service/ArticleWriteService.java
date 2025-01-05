package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.article.ArticlePostReq;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ArticleWriteService {

    /**
     * 保存or更新文章
     *
     * @param req
     * @param author 作者
     * @return
     */
    Long saveArticle(ArticlePostReq req, Long author);

    /**
     * 删除文章
     *
     * @param articleId
     */
    void deleteArticle(Long articleId, Long loginUserId);
}
