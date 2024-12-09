package cor.chrissy.community.service.article.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.service.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article_tag")
public class ArticleTagDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

    private Integer deleted;
}
