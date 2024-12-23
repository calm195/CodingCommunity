package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ColumnService {
    /**
     * 专栏列表
     *
     * @param pageParam
     * @return
     */
    PageListVo<ColumnDTO> listColumn(PageParam pageParam);

    /**
     * 获取专栏中的第N篇文章
     *
     * @param columnId
     * @param order
     * @return
     */
    Long queryColumnArticle(long columnId, Integer order);

    /**
     * 专栏详情
     *
     * @param columnId
     * @return
     */
    ColumnDTO queryColumnInfo(Long columnId);

    /**
     * 专栏 + 文章列表详情
     *
     * @param columnId
     * @return
     */
    List<SimpleArticleDTO> queryColumnArticles(long columnId);
}
