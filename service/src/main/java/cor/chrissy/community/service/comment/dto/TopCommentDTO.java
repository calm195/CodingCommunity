package cor.chrissy.community.service.comment.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶级评论DTO
 *
 * @author wx128
 * @createAt 2024/12/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopCommentDTO extends BaseCommentDTO {
    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 子评论
     */
    private List<SubCommentDTO> childComments;

    public List<SubCommentDTO> getChildComments() {
        if (childComments == null) {
            childComments = new ArrayList<>();
        }
        return childComments;
    }
}
