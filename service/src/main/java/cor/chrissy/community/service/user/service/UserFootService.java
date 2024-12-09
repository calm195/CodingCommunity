package cor.chrissy.community.service.user.service;

import cor.chrissy.community.core.enums.CollectionStatEnum;
import cor.chrissy.community.core.enums.CommentStatEnum;
import cor.chrissy.community.core.enums.PraiseStatEnum;

/**
 * @author wx128
 * @date 2024/12/9
 */
public interface UserFootService {
    /**
     * 文章收藏数
     * @param documentId
     * @return
     */
    Long queryCollentionCount(Long documentId);

    /**
     * 文章阅读数
     * @param documentId
     * @return
     */
    Long queryReadCount(Long documentId);

    /**
     * 文章评论数
     * @param documentId
     * @return
     */
    Long queryCommentCount(Long documentId);

    /**
     * 文章点赞数
     * @param documentId
     * @return
     */
    Long queryPraiseCount(Long documentId);

    /**
     * 收藏/取消收藏足迹
     * @param documentId
     * @param userId
     * @return
     */
    Integer operateCollectionFoot(Long documentId, Long userId, CollectionStatEnum statEnum);

    /**
     * 评论/删除评论足迹
     * @param documentId
     * @param userId
     * @return
     */
    Integer operateCommentFoot(Long documentId, Long userId, CommentStatEnum statEnum);

    /**
     * 点赞/取消点赞足迹
     * @param documentId
     * @param userId
     * @return
     */
    Integer operatePraiseFoot(Long documentId, Long userId, PraiseStatEnum statEnum);
}
