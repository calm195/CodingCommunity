package cor.chrissy.community.service.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.core.enums.PushStatusEnum;
import cor.chrissy.community.core.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;

/**
 * @author wx128
 * @date 2024/12/9
 */
public interface ArticleService {
    void updateArticle(ArticleDO articleDO);

    void deleteArticle(Long articleId);

    /**
     * 上线/下线文章
     *
     * @param articleId
     * @param pushStatusEnum
     */
    void operateArticle(Long articleId, PushStatusEnum pushStatusEnum);

    /**
     * 分页获取文章列表
     *
     * @param pageParam
     * @return
     */
    IPage<ArticleDO> getArticleByPage(PageParam pageParam);
}
