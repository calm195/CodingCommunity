package cor.chrissy.community.service.comment.converter;

import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.service.comment.dto.BaseCommentDTO;
import cor.chrissy.community.service.comment.dto.SubCommentDTO;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 评论信息转换工具
 *
 * @author wx128
 * @createAt 2024/12/10
 */
@Service
public class CommentConverter {

    public CommentDO toDo(CommentSaveReq req) {
        if (req == null) {
            return null;
        }
        CommentDO commentDO = new CommentDO();
        commentDO.setId(req.getCommentId());
        commentDO.setArticleId(req.getArticleId());
        commentDO.setUserId(req.getUserId());
        commentDO.setContent(req.getCommentContent());
        commentDO.setParentCommentId(req.getParentCommentId() == null ? 0L : req.getParentCommentId());
        commentDO.setTopCommentId(req.getTopCommentId() == null ? 0L : req.getTopCommentId());
        return commentDO;
    }

    public TopCommentDTO toTopCommentDTO(CommentDO commentDO) {
        if (commentDO == null) {
            return null;
        }

        TopCommentDTO topCommentDTO = new TopCommentDTO();
        parseDto(commentDO, topCommentDTO);
        topCommentDTO.setChildComments(new ArrayList<>());
        return topCommentDTO;
    }

    public SubCommentDTO toSubCommentDTO(CommentDO commentDO) {
        if (commentDO == null) {
            return null;
        }

        SubCommentDTO subCommentDTO = new SubCommentDTO();
        parseDto(commentDO, subCommentDTO);
        return subCommentDTO;
    }

    private <T extends BaseCommentDTO> void parseDto(CommentDO commentDO, T sub) {
        sub.setCommentId(commentDO.getId());
        sub.setUserId(commentDO.getUserId());
        sub.setCommentContent(commentDO.getContent());
        sub.setCommentTime(commentDO.getCreateTime().getTime());
        sub.setPraiseCount(0);
    }
}
