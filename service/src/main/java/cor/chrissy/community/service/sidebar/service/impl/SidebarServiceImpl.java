package cor.chrissy.community.service.sidebar.service.impl;

import com.google.common.base.Splitter;
import cor.chrissy.community.common.enums.ConfigTypeEnum;
import cor.chrissy.community.common.enums.SidebarStyleEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.config.dto.ConfigDTO;
import cor.chrissy.community.service.config.service.ConfigService;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.sidebar.dto.SideBarItemDto;
import cor.chrissy.community.service.sidebar.service.SidebarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
        return Arrays.asList(noticeSideBar(), columnSideBar(), hotArticles());
    }

    private SideBarDTO columnSideBar() {
        List<ConfigDTO> columnList = configService.getConfigList(ConfigTypeEnum.COLUMN);
        List<SideBarItemDto> items = new ArrayList<>(columnList.size());
        columnList.forEach(configDTO -> {

        });
        // TODO 精选教程的
        items.add(new SideBarItemDto()
                .setName("Java程序员进阶之路")
                .setTitle("这是一份通俗易懂、风趣幽默的Java学习指南，内容涵盖Java基础、Java并发编程、Java虚拟机、Java企业级开发、Java面试等核心知识点。")
                .setUrl("/column/1/1")
                .setImg("https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4ba0bc79579c488eb79df93cecd12390~tplv-k3u1fbpfcp-watermark.image")
        );
        return new SideBarDTO().setTitle("精选教程").setItems(items).setStyle(SidebarStyleEnum.COLUMN.getStyle());
    }

    private SideBarDTO aboutSideBar() {
        return new SideBarDTO().setTitle("关于社区").setContent("一个技术爱好者的交流社区。欢迎加入!").setStyle(SidebarStyleEnum.ABOUT.getStyle());
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
        List<SideBarItemDto> items = new ArrayList<>(noticeList.size());
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
            items.add(new SideBarItemDto()
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
        List<SideBarItemDto> items = vo.getList().stream().map(
                        s -> new SideBarItemDto()
                                .setTitle(s.getTitle())
                                .setUrl("/article/detail/" + s.getId())
                                .setTime(s.getCreateTime().getTime())
                )
                .collect(Collectors.toList());
        return new SideBarDTO().setTitle("热门推荐").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }

    @Override
    public SideBarDTO pdfSideBar() {
        List<ConfigDTO> pdfList = configService.getConfigList(ConfigTypeEnum.PDF);
        List<SideBarItemDto> items = new ArrayList<>(pdfList.size());
        pdfList.forEach(configDTO -> {

        });
        // TODO PDF 优质资源
        items.add(new SideBarItemDto()
                .setName("4天实战轻松玩转Docker")
                .setUrl("docker")
                .setImg("https://cdn.sanity.io/images/708bnrs8/production/ec8688d3f0426bf5cd5e99122b19c6791853564d-832x1042.png")
        );

        items.add(new SideBarItemDto()
                .setName("Java开发手册（嵩山版）")
                .setUrl("docker")
                .setImg("https://cdn.sanity.io/images/708bnrs8/production/6038f68c4a4ffa4e2e36837d4efc0d70734fb287-1021x1278.jpg")
        );
        return new SideBarDTO().setTitle("优质PDF").setItems(items).setStyle(SidebarStyleEnum.PDF.getStyle());
    }
}
