package cor.chrissy.community.service.article.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ColumnArticleReq;
import cor.chrissy.community.common.req.article.ColumnReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.ColumnDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ColumnSettingService {
    /**
     * 保存专栏
     *
     * @param columnReq
     */
    void saveColumn(ColumnReq columnReq);

    /**
     * 保存专栏文章
     *
     * @param req
     */
    void saveColumnArticle(ColumnArticleReq req);

    /**
     * 专栏文章排序
     *
     * @param columnArticleReqs
     */
    void sortColumnArticle(List<ColumnArticleReq> columnArticleReqs);

    /**
     * 删除专栏
     *
     * @param columnId
     */
    void deleteColumn(Integer columnId);

    /**
     * 删除专栏文章
     *
     * @param id
     */
    void deleteColumnArticle(Integer id);

    /**
     * 专栏列表
     *
     * @param pageParam
     * @return
     */
    PageVo<ColumnDTO> listColumn(PageParam pageParam);

    /**
     * 查询专栏的文章详情
     *
     * @param columnId
     * @return
     */
    List<ArticleDTO> queryColumnArticles(long columnId);
}
