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
@TableName("category")
public class CategoryDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 状态：0-未发布，1-已发布
     */
    private Integer status;

    private Integer deleted;
}
