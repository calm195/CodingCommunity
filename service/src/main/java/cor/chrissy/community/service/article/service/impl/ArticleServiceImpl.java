package cor.chrissy.community.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cor.chrissy.community.core.enums.PushStatusEnum;
import cor.chrissy.community.core.enums.YesOrNoEnum;
import cor.chrissy.community.core.req.PageParam;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.mapper.ArticleDetailMapper;
import cor.chrissy.community.service.article.repository.mapper.ArticleMapper;
import cor.chrissy.community.service.article.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wx128
 * @date 2024/12/9
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleDetailMapper articleDetailMapper;

    @Override
    public void updateArticle(ArticleDO articleDO) {
        ArticleDO updateArticle = articleMapper.selectById(articleDO.getId());
        if (updateArticle != null) {
            articleMapper.updateById(articleDO);
        }
    }

    @Override
    public void deleteArticle(Long articleId) {
        ArticleDO articleDTO = articleMapper.selectById(articleId);
        if (articleDTO != null) {
            articleDTO.setDeleted(YesOrNoEnum.YES.getCode());
            articleMapper.updateById(articleDTO);
        }
    }

    @Override
    public void operateArticle(Long articleId, PushStatusEnum pushStatusEnum) {
        ArticleDO articleDTO = articleMapper.selectById(articleId);
        if (articleDTO != null) {
            articleDTO.setStatus(pushStatusEnum.getCode());
            articleMapper.updateById(articleDTO);
        }
    }

    @Override
    public IPage<ArticleDO> getArticleByPage(PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode());
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        return articleMapper.selectPage(page, query);
    }
}
