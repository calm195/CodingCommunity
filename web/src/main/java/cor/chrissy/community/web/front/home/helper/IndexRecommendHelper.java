package cor.chrissy.community.web.front.home.helper;

import cor.chrissy.community.common.constant.CommonConstants;
import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.recommend.CarouseDTO;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.service.ConfigService;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.web.front.home.vo.IndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Component
public class IndexRecommendHelper {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleReadService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SidebarService sidebarService;

    @Autowired
    private ConfigService configService;

    public IndexVo buildIndexVo(String activeTab) {
        IndexVo vo = new IndexVo();
        CategoryDTO category = categories(activeTab, vo);
        vo.setArticles(articleList(category.getCategoryId()));
        vo.setTopArticles(topArticleList(category));
        vo.setHomeCarouselList(homeCarouselList());
        vo.setSideBarItems(sidebarService.queryHomeSidebarList());
        vo.setCurrentCategory(category.getCategory());
        vo.setCategoryId(category.getCategoryId());
        vo.setUser(loginInfo());
        return vo;
    }

    public IndexVo buildSearchVo(String key) {
        IndexVo vo = new IndexVo();
        vo.setArticles(articleService.queryArticlesBySearchKey(key, PageParam.newPageInstance()));
        vo.setSideBarItems(sidebarService.queryHomeSidebarList());
        return vo;
    }

    /**
     * 轮播图
     *
     * @return
     */
    private List<CarouseDTO> homeCarouselList() {
        List<ConfigDTO> configList = configService.getConfigList(ConfigTypeEnum.HOME_PAGE);
        return configList.stream()
                .map(configDTO -> new CarouseDTO()
                        .setName(configDTO.getName())
                        .setImgUrl(configDTO.getBannerUrl())
                        .setActionUrl(configDTO.getJumpUrl()))
                .collect(Collectors.toList());
    }

    /**
     * 文章列表
     */
    private PageListVo<ArticleDTO> articleList(Long categoryId) {
        return articleService.queryArticlesByCategory(categoryId, PageParam.newPageInstance());
    }


    /**
     * 返回分类列表
     *
     * @param active
     * @return
     */
    private CategoryDTO categories(String active, IndexVo vo) {
        List<CategoryDTO> allList = categoryService.loadAllCategories();
        // 查询所有分类的对应的文章数
        Map<Long, Long> articleCnt = articleService.queryArticleCountByCategory();
        // 过滤掉文章数为0的分类
        allList.removeIf(c -> articleCnt.getOrDefault(c.getCategoryId(), 0L) <= 0L);

        // 刷新选中的分类
        AtomicReference<CategoryDTO> selectedArticle = new AtomicReference<>();
        allList.forEach(category -> {
            if (category.getCategory().equalsIgnoreCase(active)) {
                category.setSelected(true);
                selectedArticle.set(category);
            } else {
                category.setSelected(false);
            }
        });

        // 添加默认的全部分类
        allList.add(0, new CategoryDTO(0L, CategoryDTO.DEFAULT_TOTAL_CATEGORY));
        if (selectedArticle.get() == null) {
            selectedArticle.set(allList.get(0));
            allList.get(0).setSelected(true);
        }

        vo.setCategories(allList);
        return selectedArticle.get();
    }

    private UserStatisticInfoDTO loginInfo() {
        if (ReqInfoContext.getReqInfo() != null && ReqInfoContext.getReqInfo().getUserId() != null) {
            return userService.queryUserInfoWithStatistic(ReqInfoContext.getReqInfo().getUserId());
        }
        return null;
    }

    /**
     * top 文章列表
     */
    private List<ArticleDTO> topArticleList(CategoryDTO category) {
        List<ArticleDTO> topArticles = articleService.queryTopArticlesByCategory(category.getCategoryId() == 0 ? null : category.getCategoryId());
        if (topArticles.size() < PageParam.TOP_PAGE_SIZE) {
            // 当分类下文章数小于置顶数时，为了避免显示问题，直接不展示
            topArticles.clear();
            return topArticles;
        }

        // 查询分类对应的头图列表
        List<String> topPicList = CommonConstants.HOMEPAGE_TOP_PIC_MAP.getOrDefault(category.getCategory(),
                CommonConstants.HOMEPAGE_TOP_PIC_MAP.get(CommonConstants.CATEGORY_ALL));

        // 替换头图，下面做了一个数组越界的保护，避免当topPageSize数量变大，但是默认的cover图没有相应增大导致数组越界异常
        AtomicInteger index = new AtomicInteger(0);
        topArticles.forEach(s -> s.setCover(topPicList.get(index.getAndIncrement() % topPicList.size())));
        return topArticles;
    }

}

