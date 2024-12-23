package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.enums.OperateArticleTypeEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.common.vo.FlagBitDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.service.ArticleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ArticleSettingServiceImpl implements ArticleSettingService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Integer getArticleCount() {
        return articleDao.countArticle();
    }

    @Override
    public PageVo<ArticleDTO> getArticleList(PageParam pageParam) {
        List<ArticleDO> articleDOS = articleDao.listArticles(pageParam);
        Integer totalCount = articleDao.countArticle();
        return PageVo.build(ArticleConverter.toArticleDtoList(articleDOS),pageParam.getPageSize(), pageParam.getPageNum(),totalCount);
    }

    @Override
    public void operateArticle(Long articleId, Integer operateType) {
        ArticleDTO articleDTO = articleDao.queryArticleDetail(articleId);
        Integer flagBit = articleDTO.getFlagBit();

        FlagBitDTO flagBitDTO = OperateArticleTypeEnum.fromCode(operateType).getFlagBit();
        if (flagBitDTO.getForward().equals(Boolean.TRUE)) {
            flagBit = flagBit | flagBitDTO.getFlagBit();
        } else {
            flagBit = flagBit & ~flagBitDTO.getFlagBit();
        }
        articleDao.updateArticleFlagBit(articleId, flagBit);
    }
}

