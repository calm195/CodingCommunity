package cor.chrissy.community.service.user.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.ArticleFootCountDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.user.repository.entity.UserFootDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserFootMapper extends BaseMapper<UserFootDO> {

    /**
     * 查询文章计数信息
     *
     * @param articleId
     * @return
     */
    ArticleFootCountDTO queryCountByArticle(@Param("articleId") Long articleId);

    /**
     * 查询用户文章计数
     *
     * @param userId
     * @return
     */
    ArticleFootCountDTO queryArticleFootCount(@Param("userId") Long userId);

    /**
     * 查询用户收藏的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<ArticleDO> queryCollectionArticleList(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);


    /**
     * 查询用户阅读的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<ArticleDO> queryReadArticleList(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);
}
