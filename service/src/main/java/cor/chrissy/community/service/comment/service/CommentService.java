package cor.chrissy.community.service.comment.service;

import cor.chrissy.community.service.comment.dto.CommentTreeDTO;
import cor.chrissy.community.common.req.comment.CommentReq;
import cor.chrissy.community.common.req.PageSearchReq;

import java.util.Map;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface CommentService {
    /**
     * 获取文章评论列表
     * @param articleId 文章ID
     * @param pageSearchReq 分页
     * @return
     */
    Map<Long, CommentTreeDTO> getCommentList(Long articleId, PageSearchReq pageSearchReq);

    /**
     * 更新/保存评论
     * @param commentReq
     * @throws Exception
     */
    void saveComment(CommentReq commentReq) throws Exception;

    /**
     * 删除评论
     * @param commentId
     * @throws Exception
     */
    void deleteComment(Long commentId) throws Exception;
}