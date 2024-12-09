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
@TableName("article")
public class ArticleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 文章类型：1-博文，2-问答
     */
    private Long articleType;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 文章头图
     */
    private String picture;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 类目ID
     */
    private Long categoryId;

    /**
     * 来源：1-转载，2-原创，3-翻译
     */
    private Integer source;

    /**
     * 状态：0-未发布，1-已发布
     */
    private Integer status;

    private Integer deleted;
}
