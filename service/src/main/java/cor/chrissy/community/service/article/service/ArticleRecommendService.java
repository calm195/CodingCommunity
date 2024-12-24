package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ArticleRecommendService {
    /**
     * 文章详情页的侧边栏推荐
     *
     * @param articleDO
     * @return
     */
    List<SideBarDTO> recommend(ArticleDTO articleDO);


    /**
     * 文章关联推荐
     *
     * @param article
     * @return
     */
    PageListVo<ArticleDTO> relatedRecommend(Long article, PageParam pageParam);
}
