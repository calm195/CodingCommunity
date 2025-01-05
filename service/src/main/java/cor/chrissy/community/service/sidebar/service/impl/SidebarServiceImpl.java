package cor.chrissy.community.service.sidebar.service.impl;

import com.google.common.base.Splitter;
import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.common.enums.SidebarStyleEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.util.JsonUtil;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.service.ConfigService;
import cor.chrissy.community.service.recommend.dto.RateVisitDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarItemDTO;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public List<SideBarDTO> queryHomeSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(noticeSideBar());
        list.add(columnSideBar());
        list.add(hotArticles());
        return list;
    }

    @Override
    public List<SideBarDTO> queryColumnSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(subscribeSideBar());
        return list;
    }

    @Override
    public List<SideBarDTO> queryArticleDetailSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(pdfSideBar());
        return list;
    }

    private SideBarDTO subscribeSideBar() {
        return new SideBarDTO().setTitle("book").setSubTitle("test")
                .setImg("")
                .setContent("books")
                .setStyle(SidebarStyleEnum.SUBSCRIBE.getStyle());
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

    private SideBarDTO recommendSideBar() {
        return new SideBarDTO().setTitle("title a").setSubTitle("sub title aa")
                .setIcon("https://tool.hhui.top/icon.svg")
                .setImg("https://spring.hhui.top/spring-blog/imgs/info/wx.jpg")
                .setContent("联系信息:<br/> test@gmail.com")
                .setStyle(SidebarStyleEnum.RECOMMEND.getStyle());

    }

    /**
     * 公告信息
     *
     * @return
     */
    private SideBarDTO noticeSideBar() {
        List<ConfigDTO> noticeList = configService.getConfigList(ConfigTypeEnum.NOTICE);
        List<SideBarItemDTO> items = new ArrayList<>(noticeList.size());
        noticeList.forEach(configDTO -> {
            List<Integer> configTags;
            if (StringUtils.isBlank(configDTO.getTags())) {
                configTags = Collections.emptyList();
            } else {
                configTags = Splitter.on(",")
                        .splitToStream(configDTO.getTags())
                        .map(s -> Integer.parseInt(s.trim()))
                        .collect(Collectors.toList());
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
                .setTitle("about")
                .setItems(items)
                .setImg("https://paicoding-oss.oss-cn-hangzhou.aliyuncs.com/paicoding-zsxq.jpg")
                .setUrl("https://www.yuque.com/itwanger/ydx81p/nksgcaox959w7ie9")
                .setStyle(SidebarStyleEnum.ABOUT.getStyle());
    }

    /**
     * 热门文章
     *
     * @return
     */
    private SideBarDTO hotArticles() {
        PageListVo<SimpleArticleDTO> vo = articleReadService.queryHotArticlesForRecommend(PageParam.newPageInstance());
        List<SideBarItemDTO> items = vo.getList().stream().map(
                        s -> new SideBarItemDTO()
                                .setTitle(s.getTitle())
                                .setUrl("/article/detail/" + s.getId())
                                .setTime(s.getCreateTime().getTime())
                )
                .collect(Collectors.toList());
        return new SideBarDTO().setTitle("热门推荐").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }

    private SideBarDTO pdfSideBar() {
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
}
