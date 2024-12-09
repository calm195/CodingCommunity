package cor.chrissy.community.service.comment.converter;

import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.service.comment.dto.CommentTreeDTO;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
        commentDO.setParentCommentId(req.getParentCommentId());
        return commentDO;
    }

    public CommentTreeDTO toDTO(CommentDO commentDO) {
        CommentTreeDTO commentTreeDTO = new CommentTreeDTO();
        commentTreeDTO.setUserId(commentDO.getUserId());
        commentTreeDTO.setCommentContent(commentDO.getContent());
        commentTreeDTO.setCommentTime(commentDO.getUpdateTime());
        commentTreeDTO.setParentCommentId(commentDO.getParentCommentId());
        commentTreeDTO.setPraiseCount(0);
        commentTreeDTO.setCommentChilds(new HashMap<>());
        return commentTreeDTO;
    }
}
