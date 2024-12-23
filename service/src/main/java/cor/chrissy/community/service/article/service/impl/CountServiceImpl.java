package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.service.article.dto.ArticleFootCountDTO;
import cor.chrissy.community.service.article.service.CountService;
import cor.chrissy.community.service.comment.service.CommentReadService;
import cor.chrissy.community.service.user.dto.SimpleUserInfoDTO;
import cor.chrissy.community.service.user.repository.dao.UserFootDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class CountServiceImpl implements CountService {

    private final UserFootDao userFootDao;

    @Resource
    private CommentReadService commentReadService;

    public CountServiceImpl(UserFootDao userFootDao) {
        this.userFootDao = userFootDao;
    }

    @Override
    public List<SimpleUserInfoDTO> queryPraiseUserInfosByArticleId(Long articleId) {
        return null;
    }

    @Override
    public ArticleFootCountDTO queryArticleCountInfoByArticleId(Long articleId) {
        ArticleFootCountDTO res = userFootDao.countArticleByArticleId(articleId);
        if (res == null) {
            res = new ArticleFootCountDTO();
        } else {
            res.setCommentCount(commentReadService.queryCommentCount(articleId));
        }
        return res;
    }


    @Override
    public ArticleFootCountDTO queryArticleCountInfoByUserId(Long userId) {
        return userFootDao.countArticleByUserId(userId);
    }

    /**
     * 查询评论的点赞数
     *
     * @param commentId
     * @return
     */
    @Override
    public Long queryCommentPraiseCount(Long commentId) {
        return userFootDao.countCommentPraise(commentId);
    }
}

