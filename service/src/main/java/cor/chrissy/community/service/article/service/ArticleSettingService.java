package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.enums.OperateArticleEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ArticleSettingService {
    /**
     * 获取文章总数
     *
     * @return
     */
    public Integer getArticleCount();

    /**
     * 获取文章列表
     *
     * @param pageParam
     * @return
     */
    PageVo<ArticleDTO> getArticleList(PageParam pageParam);


    /**
     * 操作文章
     *
     * @param articleId
     * @param operateType
     */
    void operateArticle(Long articleId, OperateArticleEnum operateType);
}
