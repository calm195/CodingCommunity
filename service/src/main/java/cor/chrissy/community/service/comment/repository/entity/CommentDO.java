package cor.chrissy.community.service.comment.repository.entity;

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
@TableName("comment")
public class CommentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String version;

    /**
     * 父评论ID
     */
    private Integer parentCommentId;

    private Integer deleted;
}