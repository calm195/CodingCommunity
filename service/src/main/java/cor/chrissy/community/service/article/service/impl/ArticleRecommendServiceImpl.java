package cor.chrissy.community.service.article.service.impl;

import cor.chrissy.community.common.enums.SidebarStyleEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.repository.dao.ArticleTagDao;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.repository.entity.ArticleTagDO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.ArticleRecommendService;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarItemDTO;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class ArticleRecommendServiceImpl implements ArticleRecommendService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleTagDao articleTagDao;

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private SidebarService sidebarService;

    @Override
    public List<SideBarDTO> recommend(ArticleDTO articleDO) {
        List<SideBarDTO> sides = sidebarService.queryArticleDetailSidebarList();

        // 推荐文章
        SideBarDTO recommend = recommendByAuthor(articleDO.getAuthor(), articleDO.getArticleId(), PageParam.DEFAULT_PAGE_SIZE);
        sides.add(recommend);

        return sides;
    }


    /**
     * 作者的文章列表推荐
     *
     * @param authorId
     * @param size
     * @return
     */
    public SideBarDTO recommendByAuthor(Long authorId, Long articleId, long size) {
        List<SimpleArticleDTO> list = articleDao.listAuthorHotArticles(authorId, PageParam.newPageInstance(PageParam.DEFAULT_PAGE_NUM, size));
        List<SideBarItemDTO> items = list.stream().filter(s -> !s.getId().equals(articleId))
                .map(s -> new SideBarItemDTO()
                        .setTitle(s.getTitle()).setUrl("/article/detail/" + s.getId())
                        .setTime(s.getCreateTime().getTime()))
                .collect(Collectors.toList());
        return new SideBarDTO().setTitle("相关文章").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }

    /**
     * 扫描关注
     *
     * @return
     */
    public SideBarDTO joinUs() {
        return new SideBarDTO().setTitle("扫码进群").setSubTitle("加入交流社区")
                .setImg("https://spring.hhui.top/spring-blog/imgs/info/wx.jpg")
                .setContent("联系信息:<br/> yihuihuiyi@gmail.com")
                .setStyle(SidebarStyleEnum.RECOMMEND.getStyle());
    }

    /**
     * 查询文章关联推荐列表
     *
     * @param articleId
     * @param page
     * @return
     */
    @Override
    public PageListVo<ArticleDTO> relatedRecommend(Long articleId, PageParam page) {
        ArticleDO article = articleDao.getById(articleId);
        if (article == null) {
            return PageListVo.emptyVo();
        }
        List<Long> tagIds = articleTagDao.listArticleTags(articleId).stream()
                .map(ArticleTagDO::getTagId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tagIds)) {
            return PageListVo.emptyVo();
        }

        List<ArticleDO> recommendArticles = articleDao.listRelatedArticlesOrderByReadCount(article.getCategoryId(), tagIds, page);
        if (recommendArticles.removeIf(s -> s.getId().equals(articleId))) {
            page.setPageSize(page.getPageSize() - 1);
        }
        return articleReadService.buildArticleListVo(recommendArticles, page.getPageSize());
    }
}
