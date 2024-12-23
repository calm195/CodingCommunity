package cor.chrissy.community.service.article.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.dto.YearArticleDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface ArticleMapper extends BaseMapper<ArticleDO> {
    /**
     * 根据阅读次数获取热门文章
     *
     * @param pageParam
     * @return
     */
    List<SimpleArticleDTO> listArticlesByReadCounts(@Param("pageParam") PageParam pageParam);

    /**
     * 查询作者的热门文章
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<SimpleArticleDTO> listArticlesByUserIdOrderByReadCounts(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);

    /**
     * 根据类目 + 标签查询文章
     *
     * @param category
     * @param tagIds
     * @param pageParam
     * @return
     */
    List<ArticleDO> listArticleByCategoryAndTags(@Param("categoryId") Long category,
                                                 @Param("tags") List<Long> tagIds,
                                                 @Param("pageParam") PageParam pageParam);

    /**
     * 根据用户ID获取创作历程
     *
     * @param userId
     * @return
     */
    List<YearArticleDTO> listYearArticleByUserId(@Param("userId") Long userId);
}
