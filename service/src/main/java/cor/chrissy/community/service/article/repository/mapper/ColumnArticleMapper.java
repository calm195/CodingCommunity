package cor.chrissy.community.service.article.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.service.article.repository.entity.ColumnArticleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface ColumnArticleMapper extends BaseMapper<ColumnArticleDO> {
    /**
     * 查询文章列表
     *
     * @param columnId
     * @return
     */
    List<Long> listColumnArticles(@Param("columnId") Long columnId);

    /**
     * 查询文章
     *
     * @param columnId
     * @param section
     * @return
     */
    Long getColumnArticle(@Param("columnId") Long columnId, @Param("section") Integer section);
}

