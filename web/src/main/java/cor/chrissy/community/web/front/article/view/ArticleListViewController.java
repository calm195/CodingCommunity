package cor.chrissy.community.web.front.article.view;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.article.service.TagService;
import cor.chrissy.community.web.front.article.vo.ArticleListVo;
import cor.chrissy.community.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wx128
 * @createAt 2024/12/24
 */
@RequestMapping(path = "article")
@Controller
public class ArticleListViewController extends BaseViewController {
    @Autowired
    private ArticleReadService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    /**
     * 查询某个分类下的文章列表
     *
     * @param category
     * @return
     */
    @GetMapping(path = "category/{category}")
    public String categoryList(@PathVariable("category") String category, Model model) {
        Long categoryId = categoryService.queryCategoryId(category);
        PageListVo<ArticleDTO> list = categoryId != null ? articleService.queryArticlesByCategory(categoryId, PageParam.newPageInstance()) : PageListVo.emptyVo();
        ArticleListVo vo = new ArticleListVo();
        vo.setArchives(category);
        vo.setArchiveId(categoryId);
        vo.setArticles(list);
        model.addAttribute("vo", vo);
        return "views/article-category-list/index";
    }

    /**
     * 查询某个标签下文章列表
     *
     * @param tag
     * @param model
     * @return
     */
    @GetMapping(path = "tag/{tag}")
    public String tagList(@PathVariable("tag") String tag, Model model) {
        Long tagId = tagService.queryTagId(tag);
        PageListVo<ArticleDTO> list = tagId != null ? articleService.queryArticlesByTag(tagId, PageParam.newPageInstance()) : PageListVo.emptyVo();
        ArticleListVo vo = new ArticleListVo();
        vo.setArchives(tag);
        vo.setArchiveId(tagId);
        vo.setArticles(list);
        model.addAttribute("vo", vo);
        return "views/article-tag-list/index";
    }
}

