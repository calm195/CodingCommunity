package cor.chrissy.community.service.user.service;

import cor.chrissy.community.common.enums.OperateTypeEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.dto.ArticleFootCountDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.comment.repository.entity.CommentDO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
public interface UserFootService {

    /**
     * 保存文章计数
     *
     * @param articleId       文章主键
     * @param author          作者
     * @param userId          操作用户
     * @param operateTypeEnum 操作类型
     * @return
     */
    ArticleFootCountDTO saveArticleFoot(Long articleId, Long author, Long userId, OperateTypeEnum operateTypeEnum);

    /**
     * 根据文章ID查询文章计数
     *
     * @param articleId
     * @return
     */
    ArticleFootCountDTO queryArticleCountByArticleId(Long articleId);


    /**
     * 根据用户ID查询文章计数
     *
     * @param userId
     * @return
     */
    ArticleFootCountDTO queryArticleCountByUserId(Long userId);

    /**
     * 获取评论点赞数量
     *
     * @param commentId
     * @return
     */
    Long queryCommentPraiseCount(Long commentId);

    /**
     * 查询已读文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<ArticleDO> queryReadArticleList(Long userId, PageParam pageParam);

    /**
     * 查询收藏文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<ArticleDO> queryCollectionArticleList(Long userId, PageParam pageParam);

    /**
     * 保存评论足迹
     *
     * @param comment 保存评论入参
     */
    void saveCommentFoot(CommentDO comment, Long articleAuthor, Long parentCommentAuthor);

    /**
     * 删除评论足迹
     *
     * @param commentDO
     * @throws Exception
     */
    void deleteCommentFoot(CommentDO commentDO, Long articleAuthor, Long parentCommentAuthor);

}
