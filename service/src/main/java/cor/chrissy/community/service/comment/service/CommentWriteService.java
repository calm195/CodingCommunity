package cor.chrissy.community.service.comment.service;

import cor.chrissy.community.common.req.comment.CommentSaveReq;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface CommentWriteService {
    /**
     * 更新/保存评论
     *
     * @param commentSaveReq
     * @return
     */
    Long saveComment(CommentSaveReq commentSaveReq);

    /**
     * 删除评论
     *
     * @param commentId
     * @throws Exception
     */
    void deleteComment(Long commentId, Long userId);
}
