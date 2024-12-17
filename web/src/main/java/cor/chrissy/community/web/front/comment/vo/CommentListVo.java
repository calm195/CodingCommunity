package cor.chrissy.community.web.front.comment.vo;

import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import lombok.Data;

import java.util.List;

/**
 * 评论返回结果
 *
 * @author wx128
 * @createAt 2024/12/16
 */
@Data
public class CommentListVo {

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 热门评论(一期先不做)
     */
    private List<TopCommentDTO> hotCommentList;

    /**
     * 评论列表
     */
    private List<TopCommentDTO> commentList;
}

