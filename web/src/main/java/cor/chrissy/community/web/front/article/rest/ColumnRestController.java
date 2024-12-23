package cor.chrissy.community.web.front.article.rest;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.NextPageHtmlVo;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
import cor.chrissy.community.service.article.service.ColumnService;
import cor.chrissy.community.web.component.TemplateEngineHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "column/api")
public class ColumnRestController {
    @Autowired
    private ColumnService columnService;

    @Autowired
    private TemplateEngineHelper templateEngineHelper;

    /**
     * 翻页的专栏列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "list")
    public Result<NextPageHtmlVo> list(@RequestParam(name = "page") Long page,
                                       @RequestParam(name = "size", required = false) Long size) {
        if (page <= 0) {
            page = 1L;
        }
        size = Optional.ofNullable(size).orElse(PageParam.DEFAULT_PAGE_SIZE);
        size = Math.min(size, PageParam.DEFAULT_PAGE_SIZE);
        PageListVo<ColumnDTO> list = columnService.listColumn(PageParam.newPageInstance(page, size));

        String html = templateEngineHelper.renderToVo("biz/column/list", "columns", list);
        return Result.ok(new NextPageHtmlVo(html, list.getHasMore()));
    }

    /**
     * 详情页的菜单栏(即专栏的文章列表)
     *
     * @param columnId
     * @return
     */
    @GetMapping(path = "menu/{column}")
    public Result<NextPageHtmlVo> columnMenus(@PathVariable("column") Long columnId) {
        List<SimpleArticleDTO> articleList = columnService.queryColumnArticles(columnId);
        String html = templateEngineHelper.renderToVo("biz/column/menus", "menu", articleList);
        return Result.ok(new NextPageHtmlVo(html, false));
    }
}

