package cor.chrissy.community.service.comment.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/10
 */
@Data
public class CommentTreeDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户图像
     */
    private String userPhoto;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 父评论ID
     */
    private Long parentCommentId;

    /**
     * 点赞数量
     */
    private Integer praiseCount;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 子评论
     */
    private Map<Long, CommentTreeDTO> commentChilds;
}
