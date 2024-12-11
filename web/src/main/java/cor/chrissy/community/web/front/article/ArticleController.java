package cor.chrissy.community.web.front.article;

import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.service.ArticleService;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.article.service.TagService;
import cor.chrissy.community.service.user.dto.UserHomeDTO;
import cor.chrissy.community.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/11
 */
@Controller
@RequestMapping(path = "article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    /**
     * 文章编辑页
     *
     * @param articleId
     * @return
     */
    @GetMapping(path = "edit")
    public String edit(@RequestParam(required = false) Long articleId, Model model) {
        if (articleId != null) {
            ArticleDTO article = articleService.queryArticleDetail(articleId);
            model.addAttribute("article", article);

            List<CategoryDTO> categoryList = categoryService.loadAllCategories(false);
            categoryList.forEach(s -> {
                s.setSelected(s.getCategoryId().equals(article.getCategory().getCategoryId()));
            });
            model.addAttribute("categories", categoryList);

            model.addAttribute("tagChooses", article.getTags());
            model.addAttribute("tags", tagService.getTagListByCategoryId(article.getCategory().getCategoryId()));
        } else {
            List<CategoryDTO> categoryList = categoryService.loadAllCategories(false);
            model.addAttribute("categories", categoryList);
            model.addAttribute("tags", Collections.emptyList());
        }

        return "biz/article/edit";
    }

    /**
     * 发布文章，完成后跳转到详情页
     *
     * @return
     */
    @PostMapping(path = "post")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> post(@RequestBody ArticlePostReq req, HttpServletResponse response) throws IOException {
        Long articleId = articleService.saveArticle(req);
        return Result.ok(articleId);
    }

    /**
     * 文章详情页
     * - 参数解析知识点
     * - * [1.Get请求参数解析姿势汇总 | 一灰灰Learning](https://hhui.top/spring-web/01.request/01.190824-springboot%E7%B3%BB%E5%88%97%E6%95%99%E7%A8%8Bweb%E7%AF%87%E4%B9%8Bget%E8%AF%B7%E6%B1%82%E5%8F%82%E6%95%B0%E8%A7%A3%E6%9E%90%E5%A7%BF%E5%8A%BF%E6%B1%87%E6%80%BB/)
     *
     * @param articleId
     * @return
     */
    @GetMapping("detail/{articleId}")
    public String detail(@PathVariable(name = "articleId") Long articleId, Model model) {
        ArticleDTO articleDTO = articleService.queryArticleDetail(articleId);
        model.addAttribute("article", articleDTO);

        // 作者信息
        UserHomeDTO user = userService.getUserHomeDTO(articleDTO.getAuthor());
        model.addAttribute("author", user);
        return "biz/article/detail";
    }

    /**
     * 查询所有的标签
     *
     * @return
     */
    @ResponseBody
    @GetMapping(path = "tag/list")
    public Result<List<TagDTO>> queryTags(Long categoryId) {
        if (categoryId == null || categoryId <= 0L) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS, categoryId);
        }

        List<TagDTO> list = tagService.getTagListByCategoryId(categoryId);
        return Result.ok(list);
    }

    /**
     * 获取所有的分类
     *
     * @return
     */
    @ResponseBody
    @GetMapping(path = "category/list")
    public Result<List<CategoryDTO>> getCategoryList(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        List<CategoryDTO> list = categoryService.loadAllCategories(false);
        if (categoryId != null) {
            list.forEach(c -> c.setSelected(c.getCategoryId().equals(categoryId)));
        }
        return Result.ok(list);
    }

}
