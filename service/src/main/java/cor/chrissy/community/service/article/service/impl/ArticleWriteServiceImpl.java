package cor.chrissy.community.service.article.service.impl;

import com.beust.ah.A;
import cor.chrissy.community.common.enums.*;
import cor.chrissy.community.common.req.article.ArticleMsgEvent;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.repository.dao.ArticleTagDao;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.service.ArticleRecommendService;
import cor.chrissy.community.service.article.service.ArticleWriteService;
import cor.chrissy.community.service.image.service.ImageService;
import cor.chrissy.community.service.user.service.ArticleWhiteListService;
import cor.chrissy.community.service.user.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Set;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ArticleWriteServiceImpl implements ArticleWriteService {

    private final ArticleDao articleDao;

    private final ArticleTagDao articleTagDao;

    @Autowired
    private UserFootService userFootService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ArticleWhiteListService articleWhiteListService;

    @Autowired
    private ArticleRecommendService articleRecommendService;

    public ArticleWriteServiceImpl(ArticleDao articleDao, ArticleTagDao articleTagDao) {
        this.articleDao = articleDao;
        this.articleTagDao = articleTagDao;
    }

    /**
     * 保存文章，当articleId存在时，表示更新记录； 不存在时，表示插入
     *
     * @param req
     * @return
     */
    @Override
    public Long saveArticle(ArticlePostReq req, Long author) {
        ArticleDO article = ArticleConverter.toArticleDo(req, author);
        String content = imageService.mdImgReplace(req.getContent());

        return transactionTemplate.execute(transactionStatus -> {
            if (NumUtil.nullOrZero(req.getArticleId())) {
                return insertArticle(article, content, req.getTagIds());
            } else {
                return updateArticle(article, content, req.getTagIds());
            }
        });
    }

    /**
     * 新建文章
     *
     * @param article
     * @param content
     * @param tags
     * @return
     */
    private Long insertArticle(ArticleDO article, String content, Set<Long> tags) {
        // article + article_detail + tag  三张表的数据变更
        if (needToReview(article)) {
            article.setStatus(PushStatEnum.REVIEW.getCode());
        }
        articleDao.save(article);
        Long articleId = article.getId();

        articleDao.saveArticleContent(articleId, content);

        articleTagDao.batchSave(articleId, tags);

        // 发布文章，阅读计数+1
        userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getAuthorId(), article.getAuthorId(), OperateTypeEnum.READ);

        // todo multiple events one time
        SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.CREATE, articleId));
        SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.ONLINE, articleId));
        return articleId;
    }

    /**
     * 更新文章
     *
     * @param article
     * @param content
     * @param tags
     * @return
     */
    private Long updateArticle(ArticleDO article, String content, Set<Long> tags) {
        boolean isToReview = article.getStatus() == PushStatEnum.REVIEW.getCode();

        if (needToReview(article)) {
            article.setStatus(PushStatEnum.REVIEW.getCode());
        }

        // 更新文章
        articleDao.updateById(article);

        // 更新内容
        articleDao.updateArticleContent(article.getId(), content, isToReview);

        // 标签更新
        articleTagDao.updateTags(article.getId(), tags);

        if (article.getStatus() == PushStatEnum.ONLINE.getCode()) {
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.ONLINE, article.getId()));
        } else if (isToReview) {
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.REVIEW, article.getId()));
        }

        return article.getId();
    }


    /**
     * 删除文章
     *
     * @param articleId
     */
    @Override
    public void deleteArticle(Long articleId, Long loginUserId) {
        ArticleDO articleDO = articleDao.getById(articleId);

        if (articleDO != null && !Objects.equals(loginUserId, articleDO.getAuthorId())) {
            articleDO.setDeleted(YesOrNoEnum.YES.getCode());
            throw ExceptionUtil.of(StatusEnum.FORBID_ERROR_MIXED, "you cannot delete the article");
        }

        if (articleDO != null && articleDO.getDeleted() != YesOrNoEnum.YES.getCode()) {
            articleDO.setDeleted(YesOrNoEnum.YES.getCode());
            articleDao.updateById(articleDO);

            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.DELETE, articleDO.getId()));
        }
    }

    private boolean needToReview(ArticleDO article) {
        return article.getStatus() == PushStatEnum.OFFLINE.getCode() || !articleWhiteListService.authorInArticleWhiteList(article.getAuthorId());
    }
}

