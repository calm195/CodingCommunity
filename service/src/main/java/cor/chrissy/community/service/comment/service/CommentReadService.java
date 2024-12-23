package cor.chrissy.community.service.comment.service;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface CommentReadService {
    /**
     * 根据评论id查询评论信息
     *
     * @param commentId
     * @return
     */
    CommentDO queryComment(Long commentId);

    /**
     * 查询文章评论列表
     *
     * @param articleId
     * @param page
     * @return
     */
    List<TopCommentDTO> getArticleComments(Long articleId, PageParam page);

    /**
     * 查询热门评论
     *
     * @param articleId
     * @return
     */
    TopCommentDTO queryHotComment(Long articleId);

    /**
     * 文章的有效评论数
     *
     * @param articleId
     * @return
     */
    int queryCommentCount(Long articleId);
}
