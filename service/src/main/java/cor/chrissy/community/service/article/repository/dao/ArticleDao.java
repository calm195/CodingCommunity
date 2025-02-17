package cor.chrissy.community.service.article.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.DocumentTypeEnum;
import cor.chrissy.community.common.enums.OfficialStatEnum;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.dto.YearArticleDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.ArticleDetailDO;
import cor.chrissy.community.service.article.repository.entity.ReadCountDO;
import cor.chrissy.community.service.article.repository.mapper.ArticleDetailMapper;
import cor.chrissy.community.service.article.repository.mapper.ArticleMapper;
import cor.chrissy.community.service.article.repository.mapper.ReadCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Repository
public class ArticleDao extends ServiceImpl<ArticleMapper, ArticleDO> {

    @Resource
    private ArticleDetailMapper articleDetailMapper;

    @Resource
    private ReadCountMapper readCountMapper;

    /**
     * 查询文章详情
     *
     * @param articleId
     * @return
     */
    public ArticleDTO queryArticleDetail(Long articleId) {
        // 查询文章记录
        ArticleDO article = baseMapper.selectById(articleId);
        if (article == null || Objects.equals(article.getDeleted(), YesOrNoEnum.YES.getCode())) {
            return null;
        }

        // 查询文章正文
        ArticleDTO dto = ArticleConverter.toDto(article);
        if (showReviewContent(article)) {
            ArticleDetailDO detail = findLatestDetail(articleId);
            dto.setContent(detail.getContent());
        } else {
            dto.setContent("## the article is being audited");
        }

        return dto;
    }

    private boolean showReviewContent(ArticleDO article) {
        if (article.getStatus() != PushStatEnum.REVIEW.getCode()) {
            return true;
        }

        BaseUserInfoDTO userInfoDTO = ReqInfoContext.getReqInfo().getUserInfo();
        if (userInfoDTO == null) {
            return false;
        }

        return userInfoDTO.getUserId().equals(article.getAuthorId())
                || (userInfoDTO.getRole() != null && userInfoDTO.getRole().equalsIgnoreCase(UserRole.ADMIN.name()));
    }

    public Long countArticleByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(ArticleDO::getCategoryId, categoryId);
        return baseMapper.selectCount(query);
    }

    // ------------ article content  ----------------

    private ArticleDetailDO findLatestDetail(long articleId) {
        // 查询文章内容
        LambdaQueryWrapper<ArticleDetailDO> contentQuery = Wrappers.lambdaQuery();
        contentQuery.eq(ArticleDetailDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDetailDO::getArticleId, articleId)
                .orderByDesc(ArticleDetailDO::getVersion);
        return articleDetailMapper.selectList(contentQuery).get(0);
    }

    /**
     * 保存文章正文
     *
     * @param articleId
     * @param content
     * @return
     */
    public Long saveArticleContent(Long articleId, String content) {
        ArticleDetailDO detail = new ArticleDetailDO();
        detail.setArticleId(articleId);
        detail.setContent(content);
        detail.setVersion(1L);
        articleDetailMapper.insert(detail);
        return detail.getId();
    }

    /**
     * 更正文章正文
     *
     * @param articleId
     * @param content
     */
    public void updateArticleContent(Long articleId, String content, boolean update) {
        if (update) {
            articleDetailMapper.updateContent(articleId, content);
        } else {
            ArticleDetailDO latest = findLatestDetail(articleId);
            latest.setVersion(latest.getVersion() + 1);
            latest.setId(null);
            latest.setContent(content);
            articleDetailMapper.insert(latest);
        }
    }

    // ------------- 文章列表查询 --------------

    public List<ArticleDO> listArticlesByAuthorId(Long authorId, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getAuthorId, authorId)
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        if (!Objects.equals(ReqInfoContext.getReqInfo().getUserId(), authorId)) {
            query.eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode());
        }
        return baseMapper.selectList(query);
    }


    public List<ArticleDO> listArticlesByCategoryId(Long categoryId, PageParam pageParam) {
        if (categoryId != null && categoryId <= 0) {
            // 分类不存在时，表示查所有
            categoryId = null;
        }
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode());

        if (pageParam.getPageSize() == PageParam.TOP_PAGE_SIZE) {
            query.eq(ArticleDO::getOfficialStat, OfficialStatEnum.OFFICIAL.getCode());
        }

        Optional.ofNullable(categoryId).ifPresent(cid -> query.eq(ArticleDO::getCategoryId, cid));
        query.last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getToppingStat, ArticleDO::getCreateTime);
        return baseMapper.selectList(query);
    }

    /**
     * 通过关键词，从标题中找出相似的进行推荐，只返回主键 + 标题
     *
     * @param key
     * @return
     */
    public List<ArticleDO> listSimpleArticlesByBySearchKey(String key) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key), v -> v.like(ArticleDO::getTitle, key).or().like(ArticleDO::getShortTitle, key));
        query.select(ArticleDO::getId, ArticleDO::getTitle, ArticleDO::getShortTitle)
                .last("limit 10")
                .orderByDesc(ArticleDO::getId);
        return baseMapper.selectList(query);
    }

    public List<ArticleDO> listArticlesByBySearchKey(String key, PageParam pageParam) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key),
                        v ->
                                v.like(ArticleDO::getTitle, key).or().like(ArticleDO::getShortTitle, key).or().like(ArticleDO::getSummary, key));
        query.last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        return baseMapper.selectList(query);
    }


    /**
     * 阅读计数
     *
     * @param articleId
     * @return
     */
    public int incrReadCount(Long articleId) {
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

    /**
     * 统计用户的文章计数
     *
     * @param userId
     * @return
     */
    public int countArticleByUser(Long userId) {
        return lambdaQuery().eq(ArticleDO::getAuthorId, userId)
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode())
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }


    /**
     * 热门文章推荐，适用于首页的侧边栏
     *
     * @param pageParam
     * @return
     */
    public List<SimpleArticleDTO> listHotArticles(PageParam pageParam) {
        return baseMapper.listArticlesByReadCounts(pageParam);
    }

    /**
     * 作者的热门文章推荐，适用于作者的详情页侧边栏
     *
     * @param userId
     * @param pageParam
     * @return
     */
    public List<SimpleArticleDTO> listAuthorHotArticles(long userId, PageParam pageParam) {
        return baseMapper.listArticlesByUserIdOrderByReadCounts(userId, pageParam);
    }

    /**
     * 根据相同的类目 + 标签进行推荐
     *
     * @param categoryId
     * @param tagIds
     * @return
     */
    public List<ArticleDO> listRelatedArticlesOrderByReadCount(Long categoryId, List<Long> tagIds, PageParam pageParam) {
        List<ReadCountDO> list = baseMapper.listArticleByCategoryAndTags(categoryId, tagIds, pageParam);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        List<Long> ids = list.stream().map(ReadCountDO::getDocumentId).collect(Collectors.toList());
        List<ArticleDO> result = baseMapper.selectBatchIds(ids);
        result.sort((o1, o2) -> {
            int i1 = ids.indexOf(o1.getId());
            int i2 = ids.indexOf(o2.getId());
            return Integer.compare(i1, i2);
        });
        return result;
    }


    /**
     * 根据用户ID获取创作历程
     *
     * @param userId
     * @return
     */
    public List<YearArticleDTO> listYearArticleByUserId(Long userId) {
        return baseMapper.listYearArticleByUserId(userId);
    }

    /**
     * 文章列表（用于后台）
     *
     * @param pageParam
     * @return
     */
    public List<ArticleDO> listArticles(PageParam pageParam) {
        return lambdaQuery()
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId)
                .list();
    }

    /**
     * 文章总数（用于后台）
     *
     * @return
     */
    public Integer countArticle() {
        return lambdaQuery()
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }

    public Map<Long, Long> countArticleByCategoryId() {
        QueryWrapper<ArticleDO> query = Wrappers.query();
        query.select("category_id, count(*) as cnt")
                .eq("deleted", YesOrNoEnum.NO.getCode())
                .eq("status", PushStatEnum.ONLINE.getCode())
                .groupBy("category_id");

        List<Map<String, Object>> mapList = baseMapper.selectMaps(query);
        Map<Long, Long> result = Maps.newHashMapWithExpectedSize(mapList.size());

        for (Map<String, Object> map : mapList) {
            Long cnt = (Long) map.get("cnt");
            if (cnt != null && cnt > 0) {
                result.put((Long) map.get("category_id"), cnt);
            }
        }
        return result;
    }
}
