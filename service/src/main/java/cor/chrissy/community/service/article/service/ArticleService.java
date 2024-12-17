package cor.chrissy.community.service.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.ArticleListDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface ArticleService {

    ArticleDO querySimpleArticleById(Long id);

    /**
     * 查询文章详情
     *
     * @param articleId
     * @param updateReadCnd true表示计数加一，false则不变
     * @return
     */
    ArticleDTO queryArticleDetail(Long articleId, boolean updateReadCnd);

    /**
     * 查询某一个分类下的文章，支持翻页
     *
     * @param categoryId
     * @param pageParam
     * @return
     */
    ArticleListDTO queryArticlesByCategory(Long categoryId, PageParam pageParam);

    /**
     * 根据查询关键词查询文章，返回符合的文章列表，支持翻页
     *
     * @param key
     * @param pageParam
     * @return
     */
    ArticleListDTO queryArticlesBySearchKey(String key, PageParam pageParam);

    /**
     * 保存or更新文章
     *
     * @param req
     * @return
     */
    Long saveArticle(ArticlePostReq req);

    /**
     * 删除文章
     *
     * @param articleId
     */
    void deleteArticle(Long articleId);

    /**
     * 上线/下线文章
     *
     * @param articleId
     * @param pushStatusEnum
     */
    void operateArticle(Long articleId, PushStatEnum pushStatusEnum);

    /**
     * 分页获取文章列表
     *
     * @param pageParam
     * @return
     */
    IPage<ArticleDO> getArticleByPage(PageParam pageParam);

    /**
     * 获取用户文章列表
     *
     * @param userId
     * @return
     */
    ArticleListDTO getArticleListByUserId(Long userId, PageParam pageSearchReq);


    /**
     * 获取用户收藏的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    ArticleListDTO getCollectionArticleListByUserId(Long userId, PageParam pageParam);

    /**
     * 获取用户阅读的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    ArticleListDTO getReadArticleListByUserId(Long userId, PageParam pageParam);
}
