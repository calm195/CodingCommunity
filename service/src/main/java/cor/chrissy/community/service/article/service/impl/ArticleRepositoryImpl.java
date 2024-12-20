package cor.chrissy.community.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cor.chrissy.community.common.enums.DocumentTypeEnum;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.ArticleDetailDO;
import cor.chrissy.community.service.article.repository.entity.ArticleTagDO;
import cor.chrissy.community.service.article.repository.entity.ReadCountDO;
import cor.chrissy.community.service.article.repository.mapper.ArticleDetailMapper;
import cor.chrissy.community.service.article.repository.mapper.ArticleMapper;
import cor.chrissy.community.service.article.repository.mapper.ArticleTagMapper;
import cor.chrissy.community.service.article.repository.mapper.ReadCountMapper;
import cor.chrissy.community.service.article.service.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleDetailMapper articleDetailMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleConverter articleConverter;
    @Autowired
    private ReadCountMapper readCountMapper;

    /**
     * 查询文章详情
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO queryArticleDetail(Long articleId) {
        // 查询文章记录
        ArticleDO article = articleMapper.selectById(articleId);
        if (article == null) {
            return null;
        }


        // 查询文章关联标签
        ArticleDTO dto = articleConverter.toDTO(article);
        ArticleDetailDO detail = findLatestDetail(articleId);
        dto.setContent(detail.getContent());
        return dto;
    }

    private ArticleDetailDO findLatestDetail(long articleId) {
        // 查询文章内容
        LambdaQueryWrapper<ArticleDetailDO> contentQuery = Wrappers.lambdaQuery();
        contentQuery.eq(ArticleDetailDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDetailDO::getArticleId, articleId)
                .orderByDesc(ArticleDetailDO::getVersion);
        return articleDetailMapper.selectOne(contentQuery);
    }

    @Override
    public Long saveArticle(ArticleDO article, String content, Set<Long> tags) {
        if (article.getId() != null) {
            updateArticle(article, content, tags);
        } else {
            insertArticle(article, content, tags);
        }
        return article.getId();
    }

    private void updateArticle(ArticleDO article, String content, Set<Long> tags) {
        // 更新文章
        articleMapper.updateById(article);
        // 更新内容
        doUpdateArticleDetail(article.getId(), content);

        // 标签更新，先查出之前的标签列表
        updateTags(article.getId(), tags);
    }

    private void doUpdateArticleDetail(long articleId, String content) {
        articleDetailMapper.updateContent(articleId, content);
    }

    private void updateTags(Long articleId, Set<Long> newTags) {
        List<ArticleTagDO> dbTags = articleTagMapper.queryArticleTags(articleId);
        // 在旧的里面，不在新的里面的标签，设置为删除
        List<Long> toDeleted = new ArrayList<>();
        dbTags.forEach(tag -> {
            if (!newTags.contains(tag.getTagId())) {
                toDeleted.add(tag.getId());
            } else {
                // 移除已经存在的记录
                newTags.remove(tag.getTagId());
            }
        });
        if (!toDeleted.isEmpty()) {
            articleTagMapper.deleteBatchIds(toDeleted);
        }

        if (!newTags.isEmpty()) {
            saveTags(articleId, newTags);
        }
    }

    private void insertArticle(ArticleDO article, String content, Set<Long> tags) {
        articleMapper.insert(article);
        Long articleId = article.getId();

        ArticleDetailDO detail = new ArticleDetailDO();
        detail.setArticleId(articleId);
        detail.setContent(content);
        detail.setVersion(1L);
        articleDetailMapper.insert(detail);

        saveTags(articleId, tags);
    }

    private void saveTags(Long articleId, Set<Long> tags) {
        List<ArticleTagDO> insertList = new ArrayList<>(tags.size());
        tags.forEach(s -> {
            ArticleTagDO tag = new ArticleTagDO();
            tag.setTagId(s);
            tag.setArticleId(articleId);
            tag.setDeleted(YesOrNoEnum.NO.getCode());
            insertList.add(tag);
        });
        articleTagMapper.batchInsert(insertList);
    }

    @Override
    public ArticleDO getSimpleArticleById(Long articleId) {
        return articleMapper.selectById(articleId);
    }

    @Override
    public List<ArticleDO> getArticleListByUserId(Long userId, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(ArticleDO::getAuthorId, userId)
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        return articleMapper.selectList(query);
    }

    @Override
    public List<ArticleDO> getArticleListByCategoryId(Long categoryId, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode());
        Optional.ofNullable(categoryId).ifPresent(cid -> query.eq(ArticleDO::getCategoryId, cid));
        query.last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        return articleMapper.selectList(query);
    }

    @Override
    public List<ArticleDO> getArticleListByBySearchKey(String key, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key),
                        v -> v.like(ArticleDO::getTitle, key)
                                .or()
                                .like(ArticleDO::getShortTitle, key)
                                .or()
                                .like(ArticleDO::getSummary, key));
        query.last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        return articleMapper.selectList(query);
    }

    @Override
    public int count(Long articleId) {
        LambdaQueryWrapper<ReadCountDO> query = Wrappers.lambdaQuery();
        query.eq(ReadCountDO::getDocumentId, articleId).eq(ReadCountDO::getDocumentType, DocumentTypeEnum.ARTICLE.getCode());
        ReadCountDO record = readCountMapper.selectOne(query);
        if (record == null) {
            record = new ReadCountDO().setDocumentId(articleId).setDocumentType(DocumentTypeEnum.ARTICLE.getCode()).setCnt(1);
            readCountMapper.insert(record);
        } else {
            // fixme: 这里存在并发覆盖问题，推荐使用 update read_count set cnt = cnt + 1 where id = xxx
            record.setCnt(record.getCnt() + 1);
            readCountMapper.updateById(record);
        }
        return record.getCnt();
    }
}
