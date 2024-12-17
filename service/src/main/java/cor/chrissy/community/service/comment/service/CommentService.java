package cor.chrissy.community.service.comment.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.comment.CommentSaveReq;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface CommentService {

    /**
     * 获取文章评论
     *
     * @param articleId
     * @param pageParam
     * @return
     */
    List<TopCommentDTO> getArticleComments(Long articleId, PageParam pageParam);

    /**
     * 更新/保存评论
     *
     * @param commentSaveReq
     * @throws Exception
     */
    Long saveComment(CommentSaveReq commentSaveReq);

    /**
     * 删除评论
     *
     * @param commentId
     * @throws Exception
     */
    void deleteComment(Long commentId) throws Exception;
}
