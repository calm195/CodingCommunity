package cor.chrissy.community.service.sidebar.service.impl;

import com.google.common.base.Splitter;
import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.common.enums.SidebarStyleEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.util.JsonUtil;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.repository.dao.ArticleDao;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.service.ConfigService;
import cor.chrissy.community.service.recommend.dto.RateVisitDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarItemDTO;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@Service
public class SidebarServiceImpl implements SidebarService {

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private ConfigService configService;
    @Autowired
    private ArticleDao articleDao;


    @Override
    @Cacheable(key = "'homeSidebar'", cacheManager = "caffeineCacheManager", cacheNames = "home")
    public List<SideBarDTO> queryHomeSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(noticeSideBar());
        list.add(columnSideBar());
        list.add(hotArticles());
        return list;
    }

    @Override
    @Cacheable(key = "'columnSidebar'", cacheManager = "caffeineCacheManager", cacheNames = "colomn")
    public List<SideBarDTO> queryColumnSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(subscribeSideBar());
        return list;
    }

    @Override
    @Cacheable(key = "'sideBar_' + #articleId", cacheManager = "caffeineCacheManager", cacheNames = "article")
    public List<SideBarDTO> queryArticleDetailSidebarList(Long authorId, Long articleId) {
        List<SideBarDTO> list = new ArrayList<>(2);
        list.add(SpringUtil.getBean(SidebarServiceImpl.class).pdfSideBar());
        list.add(recommendByAuthor(authorId, articleId, PageParam.DEFAULT_PAGE_SIZE));
        return list;
    }

    @Cacheable(key = "'sideBar'", cacheManager = "caffeineCacheManager", cacheNames = "article")
    public SideBarDTO pdfSideBar() {
        List<ConfigDTO> pdfList = configService.getConfigList(ConfigTypeEnum.PDF);
        List<SideBarItemDTO> items = new ArrayList<>(pdfList.size());
        pdfList.forEach(configDTO -> {
            SideBarItemDTO dto = new SideBarItemDTO();
            dto.setName(configDTO.getName());
            dto.setUrl(configDTO.getJumpUrl());
            dto.setImg(configDTO.getBannerUrl());
            RateVisitDTO visit;
            if (StringUtils.isNotBlank(configDTO.getExtra())) {
                visit = (JsonUtil.toObj(configDTO.getExtra(), RateVisitDTO.class));
            } else {
                visit = new RateVisitDTO();
            }
            visit.incrVisit();
            // 更新阅读计数
            configService.updateVisit(configDTO.getId(), JsonUtil.toStr(visit));
            dto.setVisit(visit);
            items.add(dto);
        });
        return new SideBarDTO().setTitle("优质PDF").setItems(items).setStyle(SidebarStyleEnum.PDF.getStyle());
    }

    public SideBarDTO recommendByAuthor(Long authorId, Long articleId, long pageSize) {
        List<SimpleArticleDTO> list = articleDao.listAuthorHotArticles(authorId, PageParam.newPageInstance(PageParam.DEFAULT_PAGE_NUM, pageSize));
        List<SideBarItemDTO> items = list.stream()
                .filter(s -> !s.getId().equals(articleId))
                .map(s -> new SideBarItemDTO()
                        .setTitle(s.getTitle())
                        .setUrl("/article/detail/" + s.getId())
                        .setTime(s.getCreateTime().getTime()))
                .collect(Collectors.toList());
        return new SideBarDTO().setTitle("recommend").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }

    private SideBarDTO columnSideBar() {
        List<ConfigDTO> columnList = configService.getConfigList(ConfigTypeEnum.COLUMN);
        List<SideBarItemDTO> items = new ArrayList<>(columnList.size());
        columnList.forEach(configDTO -> {
            SideBarItemDTO item = new SideBarItemDTO();
            item.setName(configDTO.getName());
            item.setTitle(configDTO.getContent());
            item.setUrl(configDTO.getJumpUrl());
            item.setImg(configDTO.getBannerUrl());
            items.add(item);
        });
        return new SideBarDTO().setTitle("精选").setItems(items).setStyle(SidebarStyleEnum.COLUMN.getStyle());
    }

    /**
     * 热门文章
     *
     * @return
     */
    private SideBarDTO subscribeSideBar() {
        return new SideBarDTO()
                .setTitle("subscribe")
                .setSubTitle("test")
                .setImg("")
                .setContent("content test")
                .setStyle(SidebarStyleEnum.SUBSCRIBE.getStyle());
    }

    private SideBarDTO hotArticles() {
        PageListVo<SimpleArticleDTO> vo = articleReadService.queryHotArticlesForRecommend(PageParam.newPageInstance(1, 8));
        List<SideBarItemDTO> items = vo.getList().stream().map(s -> new SideBarItemDTO().setTitle(s.getTitle()).setUrl("/article/detail/" + s.getId()).setTime(s.getCreateTime().getTime())).collect(Collectors.toList());
        return new SideBarDTO().setTitle("热门文章").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }

    private SideBarDTO noticeSideBar() {
        List<ConfigDTO> noticeList = configService.getConfigList(ConfigTypeEnum.NOTICE);
        List<SideBarItemDTO> items = new ArrayList<>(noticeList.size());
        noticeList.forEach(configDTO -> {
            List<Integer> configTags;
            if (StringUtils.isBlank(configDTO.getTags())) {
                configTags = Collections.emptyList();
            } else {
                configTags = Splitter.on(",").splitToStream(configDTO.getTags()).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            }
            items.add(new SideBarItemDTO()
                    .setName(configDTO.getName())
                    .setTitle(configDTO.getContent())
                    .setUrl(configDTO.getJumpUrl())
                    .setTime(configDTO.getCreateTime().getTime())
                    .setTags(configTags)
            );
        });
        return new SideBarDTO()
                .setTitle("关于技术派")
                // TODO 知识星球的
                .setImg("https://cdn.tobebetterjavaer.com/paicoding/main/paicoding-zsxq.jpg")
                .setUrl("https://paicoding.com/article/detail/169")
                .setItems(items)
                .setStyle(SidebarStyleEnum.NOTICE.getStyle());
    }
}
