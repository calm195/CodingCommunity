package cor.chrissy.community.service.article.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import cor.chrissy.community.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("column_article")
public class ColumnArticleDO extends BaseDO {
    private static final long serialVersionUID = -2372103913090667453L;

    private Long columnId;

    private Long articleId;

    /**
     * 顺序，越小越靠前
     */
    private Integer section;
}
