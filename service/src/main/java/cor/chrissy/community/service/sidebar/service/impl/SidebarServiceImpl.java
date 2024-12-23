package cor.chrissy.community.service.sidebar.service.impl;

import cor.chrissy.community.common.enums.SidebarStyleEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarItemDto;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public List<SideBarDTO> queryHomeSidebarList() {
        return Arrays.asList(noticeSideBar(), hotArticles(), recommendSideBar(), aboutSideBar());
    }

    private SideBarDTO aboutSideBar() {
        return new SideBarDTO().setTitle("关于社区").setContent("一个技术爱好者的交流社区").setStyle(SidebarStyleEnum.ABOUT.getStyle());
    }

    private SideBarDTO recommendSideBar() {
        return new SideBarDTO().setTitle("title a").setSubTitle("sub title aa")
                .setIcon("https://tool.hhui.top/icon.svg")
                .setImg("")
                .setContent("联系信息:<br/> test@gmail.com")
                .setStyle(SidebarStyleEnum.RECOMMEND.getStyle());

    }

    /**
     * 公告信息
     *
     * @return
     */
    private SideBarDTO noticeSideBar() {
        List<SideBarItemDto> items = new ArrayList<>();
        items.add(new SideBarItemDto().setTitle("学习加油站点 - Java程序员进阶之路").setUrl("https://tobebetterjavaer.com/").setTime(System.currentTimeMillis()));
        items.add(new SideBarItemDto().setTitle("学习加油站点 - chrissy的站点").setUrl("").setTime(System.currentTimeMillis()));
        return new SideBarDTO().setTitle("公告").setItems(items).setStyle(SidebarStyleEnum.NOTICE.getStyle());
    }

    /**
     * 热门文章
     *
     * @return
     */
    private SideBarDTO hotArticles() {
        PageListVo<SimpleArticleDTO> vo = articleReadService.queryHotArticlesForRecommend(PageParam.newPageInstance());
        List<SideBarItemDto> items = vo.getList().stream().map(
                s -> new SideBarItemDto()
                        .setTitle(s.getTitle())
                        .setUrl("/article/detail/" + s.getId())
                        .setTime(s.getCreateTime().getTime())
                )
                .collect(Collectors.toList());
        return new SideBarDTO().setTitle("热门推荐").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }
}
