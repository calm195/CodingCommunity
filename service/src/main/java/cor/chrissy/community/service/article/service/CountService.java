package cor.chrissy.community.service.article.service;

import cor.chrissy.community.service.article.dto.ArticleFootCountDTO;
import cor.chrissy.community.service.user.dto.SimpleUserInfoDTO;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
public interface CountService {
    /**
     * 查询文章的点赞用户头像
     *
     * @param articleId
     * @return
     */
    List<SimpleUserInfoDTO> queryPraiseUserInfosByArticleId(Long articleId);

    /**
     * 根据文章ID查询文章计数
     *
     * @param articleId
     * @return
     */
    ArticleFootCountDTO queryArticleCountInfoByArticleId(Long articleId);


    /**
     * 查询做的总阅读相关计数（当前返回评论数）
     *
     * @param userId
     * @return
     */
    ArticleFootCountDTO queryArticleCountInfoByUserId(Long userId);

    /**
     * 获取评论点赞数量
     *
     * @param commentId
     * @return
     */
    Long queryCommentPraiseCount(Long commentId);
}

