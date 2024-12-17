package cor.chrissy.community.service.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.enums.ArticleTypeEnum;
import cor.chrissy.community.common.enums.OperateTypeEnum;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.YesOrNoEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.service.article.conveter.ArticleConverter;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.ArticleListDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.mapper.ArticleMapper;
import cor.chrissy.community.service.article.repository.mapper.ArticleTagMapper;
import cor.chrissy.community.service.article.service.ArticleRepository;
import cor.chrissy.community.service.article.service.ArticleService;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.article.service.TagService;
import cor.chrissy.community.service.user.service.UserFootService;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/9
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleRepository articleRepository;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private TagService tagService;

    @Resource
    private ArticleConverter articleConverter;

    @Resource
    private UserFootService userFootService;

    @Resource
    private UserService userService;

    @Override
    public ArticleDO querySimpleArticleById(Long articleId) {
        return articleRepository.getSimpleArticleById(articleId);
    }

    /**
     * 获取文章详情
     *
     * @param articleId
     * @param updateReadCnt
     * @return
     */
    @Override
    public ArticleDTO queryArticleDetail(Long articleId, boolean updateReadCnt) {
        ArticleDTO article = articleRepository.queryArticleDetail(articleId);

        if (article == null) {
            throw new IllegalArgumentException("文章不存在");
        }

        if (updateReadCnt) {
            articleRepository.count(articleId);
        }

        // 更新分类
        CategoryDTO category = article.getCategory();
        category.setCategory(categoryService.getCategoryName(category.getCategoryId()));

        // 更新tagIds
        article.setTags(articleTagMapper.queryArticleTagDetails(articleId));

        article.setArticleFootCount(userFootService.saveArticleFoot(articleId, article.getAuthor(), ReqInfoContext.getReqInfo().getUserId(), OperateTypeEnum.READ));
        return article;
    }

    /**
     * 保存文章，当articleId存在时，表示更新记录； 不存在时，表示插入
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveArticle(ArticlePostReq req) {
        ArticleDO article = new ArticleDO();
        // 设置作者ID
        article.setAuthorId(ReqInfoContext.getReqInfo().getUserId());
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setShortTitle(req.getSubTitle());
        article.setArticleType(ArticleTypeEnum.valueOf(req.getArticleType().toUpperCase()).getCode());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setSource(req.getSource());
        article.setSourceUrl(req.getSourceUrl());
        article.setSummary(req.getSummary());
        article.setStatus(req.pushStatus().getCode());
        article.setDeleted(req.deleted() ? 1 : 0);

        return articleRepository.saveArticle(article, req.getContent(), req.getTagIds());
    }

    @Override
    public ArticleListDTO queryArticlesByCategory(Long categoryId, PageParam page) {
        if (categoryId != null && categoryId <= 0) {
            categoryId = null;
        }
        List<ArticleDO> records = articleRepository.getArticleListByCategoryId(categoryId, page);
        List<ArticleDTO> result = new ArrayList<>();
        records.forEach(record -> {
            ArticleDTO dto = articleConverter.toDTO(record);
            dto.setArticleFootCount(userFootService.queryArticleCountByArticleId(record.getId()));
            dto.setAuthorName(userService.getUserInfoByUserId(dto.getAuthor()).getUserName());
            dto.setTags(articleTagMapper.queryArticleTagDetails(record.getId()));
            result.add(dto);
        });

        ArticleListDTO dto = new ArticleListDTO();
        dto.setArticleList(result);
        dto.setIsMore(result.size() == page.getPageSize());
        return dto;
    }

    @Override
    public ArticleListDTO queryArticlesBySearchKey(String key, PageParam page) {
        if (key == null || key.isEmpty()){
            return new ArticleListDTO();
        }

        List<ArticleDO> records = articleRepository.getArticleListByBySearchKey(key, page);
        List<ArticleDTO> result = new ArrayList<>();
        records.forEach(record -> {
            ArticleDTO dto = articleConverter.toDTO(record);
            // 阅读计数
            dto.setArticleFootCount(userFootService.queryArticleCountByArticleId(record.getId()));
            // 作者信息
            dto.setAuthorName(userService.getUserInfoByUserId(dto.getAuthor()).getUserName());
            // 标签列表
            dto.setTags(articleTagMapper.queryArticleTagDetails(record.getId()));
            result.add(dto);
        });

        ArticleListDTO dto = new ArticleListDTO();
        dto.setArticleList(result);
        dto.setIsMore(result.size() == page.getPageSize());
        return dto;
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
    public void operateArticle(Long articleId, PushStatEnum pushStatusEnum) {
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
                .eq(ArticleDO::getStatus, PushStatEnum.ONLINE.getCode());
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        return articleMapper.selectPage(page, query);
    }

    @Override
    public ArticleListDTO getArticleListByUserId(Long userId, PageParam pageParam) {

        ArticleListDTO articleListDTO = new ArticleListDTO();
        List<ArticleDO> articleDTOS = articleRepository.getArticleListByUserId(userId, pageParam);
        if (articleDTOS.isEmpty()) {
            articleListDTO.setIsMore(Boolean.FALSE);
            return articleListDTO;
        }

        BaseUserInfoDTO userInfoDTO = userService.getUserInfoByUserId(userId);

        List<ArticleDTO> articleList = new ArrayList<>();
        for (ArticleDO articleDTO : articleDTOS) {
            ArticleDTO dto = articleConverter.toDTO(articleDTO);
            // 阅读计数
            dto.setArticleFootCount(userFootService.queryArticleCountByArticleId(articleDTO.getId()));
            // 作者信息
            dto.setAuthorName(userInfoDTO.getUserName());
            // 标签列表
            dto.setTags(articleTagMapper.queryArticleTagDetails(articleDTO.getId()));
            articleList.add(dto);
        }

        Boolean isMore = (articleList.size() == pageParam.getPageSize()) ? Boolean.TRUE : Boolean.FALSE;

        articleListDTO.setArticleList(articleList);
        articleListDTO.setIsMore(isMore);
        return articleListDTO;
    }

    @Override
    public ArticleListDTO getCollectionArticleListByUserId(Long userId, PageParam pageParam) {
        ArticleListDTO articleListDTO = new ArticleListDTO();

        BaseUserInfoDTO userInfoDTO = userService.getUserInfoByUserId(userId);
        List<ArticleDO> articleDTOS = userFootService.queryCollectionArticleList(userId, pageParam);
        if (articleDTOS.isEmpty()) {
            articleListDTO.setIsMore(Boolean.FALSE);
            return articleListDTO;
        }

        List<ArticleDTO> articleList = new ArrayList<>();
        for (ArticleDO articleDTO : articleDTOS) {
            ArticleDTO dto = articleConverter.toDTO(articleDTO);
            // 阅读计数
            dto.setArticleFootCount(userFootService.queryArticleCountByArticleId(articleDTO.getId()));
            // 作者信息
            dto.setAuthorName(userInfoDTO.getUserName());
            // 标签列表
            dto.setTags(articleTagMapper.queryArticleTagDetails(articleDTO.getId()));
            articleList.add(dto);
        }

        Boolean isMore = (articleList.size() == pageParam.getPageSize()) ? Boolean.TRUE : Boolean.FALSE;

        articleListDTO.setArticleList(articleList);
        articleListDTO.setIsMore(isMore);
        return articleListDTO;
    }

    @Override
    public ArticleListDTO getReadArticleListByUserId(Long userId, PageParam pageParam) {
        ArticleListDTO articleListDTO = new ArticleListDTO();

        // 获取用户信息
        BaseUserInfoDTO userInfoDTO = userService.getUserInfoByUserId(userId);

        List<ArticleDO> articleDTOS = userFootService.queryReadArticleList(userId, pageParam);
        if (articleDTOS.isEmpty()) {
            articleListDTO.setIsMore(Boolean.FALSE);
            return articleListDTO;
        }

        List<ArticleDTO> articleList = new ArrayList<>();
        for (ArticleDO articleDTO : articleDTOS) {
            ArticleDTO dto = articleConverter.toDTO(articleDTO);
            // 阅读计数
            dto.setArticleFootCount(userFootService.queryArticleCountByArticleId(articleDTO.getId()));
            // 作者信息
            dto.setAuthorName(userInfoDTO.getUserName());
            // 标签列表
            dto.setTags(articleTagMapper.queryArticleTagDetails(articleDTO.getId()));
            articleList.add(dto);
        }

        Boolean isMore = (articleList.size() == pageParam.getPageSize()) ? Boolean.TRUE : Boolean.FALSE;

        articleListDTO.setArticleList(articleList);
        articleListDTO.setIsMore(isMore);
        return articleListDTO;
    }
}
