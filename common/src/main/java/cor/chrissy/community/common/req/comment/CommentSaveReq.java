package cor.chrissy.community.common.req.comment;

import lombok.Data;

/**
 * 评论保存请求体
 *
 * @author wx128
 * @createAt 2024/12/10
 */
@Data
public class CommentSaveReq {

    /**
     * 评论ID
     */
    private Long commentId;

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
    private String commentContent;

    /**
     * 父评论ID
     */
    private Long parentCommentId;

    /**
     * 顶级评论ID
     */
    private Long topCommentId;
}
