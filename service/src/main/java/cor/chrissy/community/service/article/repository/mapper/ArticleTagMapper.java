package cor.chrissy.community.service.article.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleTagDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTagDO> {
    /**
     * 查询文章标签
     *
     * @param articleId
     * @return
     */
    List<TagDTO> listArticleTagDetails(@Param("articleId") Long articleId);
}
