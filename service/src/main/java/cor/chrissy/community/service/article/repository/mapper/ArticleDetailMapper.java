package cor.chrissy.community.service.article.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cor.chrissy.community.service.article.repository.entity.ArticleDetailDO;
import org.apache.ibatis.annotations.Update;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface ArticleDetailMapper extends BaseMapper<ArticleDetailDO> {
    /**
     * 更新正文
     *
     * @param articleId
     * @param content
     * @return
     */
    @Update("update article_detail set `content` = #{content}, `versoin` = `version` + 1 where article_id = #{articleId} and `deleted`=0 order by `versoin` desc limit 1")
    int updateContent(long articleId, String content);

}
