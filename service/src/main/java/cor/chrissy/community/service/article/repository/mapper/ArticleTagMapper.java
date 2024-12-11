package cor.chrissy.community.service.article.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.service.article.repository.entity.ArticleTagDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTagDO> {
    /**
     * 批量保存
     *
     * @param entityList
     * @return
     */
    @Insert("<script>" +
            "insert into article_tag(`article_id`, `tag_id`, `deleted`) values " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.categoryId}, #{item.tagId}, 0})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<ArticleTagDO> entityList);
}
