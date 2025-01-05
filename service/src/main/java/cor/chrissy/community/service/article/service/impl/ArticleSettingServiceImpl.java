package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.OperateArticleEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.ExceptionUtil;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.service.ArticleSettingService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ArticleSettingServiceImpl implements ArticleSettingService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserService userService;

    @Override
    public Integer getArticleCount() {
        return articleDao.countArticle();
    }

    @Override
    public PageVo<ArticleDTO> getArticleList(PageParam pageParam) {
        List<ArticleDO> articleDOS = articleDao.listArticles(pageParam);
        List<ArticleDTO> articleDTOS = ArticleConverter.toArticleDtoList(articleDOS);
        articleDTOS.forEach(articleDTO -> {
            BaseUserInfoDTO user = userService.queryBasicUserInfo(articleDTO.getAuthor());
            articleDTO.setAuthorName(user.getUserName());
        });
        Integer totalCount = articleDao.countArticle();
        return PageVo.build(articleDTOS, pageParam.getPageSize(), pageParam.getPageNum(), totalCount);
    }

    @Override
    public void operateArticle(Long articleId, OperateArticleEnum operateType) {
        ArticleDO articleDO = articleDao.getById(articleId);
        if (articleDO == null) {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, "article not exists");
        }

        setArticleStat(articleDO, operateType);
        articleDao.updateById(articleDO);
    }

    @Override
    public void updateArticle(ArticlePostReq req) {
        ArticleDO articleDO = articleDao.getById(req.getArticleId());
        if (articleDO != null) {
            articleDO.setShortTitle(req.getShortTitle());
            articleDao.updateById(articleDO);
        }
    }

    @Override
    public void deleteArticle(Long articleId) {
        ArticleDO articleDO = articleDao.getById(articleId);
        if (articleDO != null && articleDO.getDeleted() != YesOrNoEnum.YES.getCode()) {
            articleDO.setDeleted(YesOrNoEnum.YES.getCode());
            articleDao.updateById(articleDO);
        }
    }

    private boolean setArticleStat(ArticleDO articleDO, OperateArticleEnum operate) {
        switch (operate) {
            case OFFICIAL:
            case CANCEL_OFFICIAL:
                return compareAndUpdate(articleDO::getOfficialStat, articleDO::setOfficialStat, operate.getDbStatCode());
            case TOPPING:
            case CANCEL_TOPPING:
                return compareAndUpdate(articleDO::getToppingStat, articleDO::setToppingStat, operate.getDbStatCode());
            case CREAM:
            case CANCEL_CREAM:
                return compareAndUpdate(articleDO::getCreamStat, articleDO::setCreamStat, operate.getDbStatCode());
            default:
                return false;
        }
    }

    /**
     * 相同则直接返回false不用更新；不同则更新,返回true
     *
     * @param supplier
     * @param consumer
     * @param input
     * @param <T>
     * @return
     */
    private <T> boolean compareAndUpdate(Supplier<T> supplier, Consumer<T> consumer, T input) {
        if (Objects.equals(supplier.get(), input)) {
            return false;
        }
        consumer.accept(input);
        return true;
    }
}

