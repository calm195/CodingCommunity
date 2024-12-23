package cor.chrissy.community.web.front.article.view;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.ArticleRecommendService;
import cor.chrissy.community.service.article.service.CategoryService;
import cor.chrissy.community.service.article.service.TagService;
import cor.chrissy.community.service.comment.dto.TopCommentDTO;
import cor.chrissy.community.service.comment.service.CommentReadService;
import cor.chrissy.community.service.sidebar.dto.SideBarDTO;
import cor.chrissy.community.service.user.dto.UserStatisticInfoDTO;
import cor.chrissy.community.service.user.service.UserService;
import cor.chrissy.community.web.front.article.vo.ArticleDetailVo;
import cor.chrissy.community.web.front.article.vo.ArticleEditVo;
import cor.chrissy.community.web.global.BaseViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@Controller
@RequestMapping(path = "article")
public class ArticleViewController extends BaseViewController {
    @Autowired
    private ArticleReadService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentReadService commentService;

    @Autowired
    private ArticleRecommendService articleRecommendService;

    /**
     * 文章编辑页
     *
     * @param articleId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "edit")
    public String edit(@RequestParam(required = false) Long articleId, Model model) {
        ArticleEditVo vo = new ArticleEditVo();
        if (articleId != null) {
            ArticleDTO article = articleService.queryDetailArticleInfo(articleId);
            vo.setArticle(article);
            if (!Objects.equals(article.getAuthor(), ReqInfoContext.getReqInfo().getUserId())) {
                // 没有权限
                model.addAttribute("toast", "内容不存在");
                return "redirect:403";
            }

            List<CategoryDTO> categoryList = categoryService.loadAllCategories();
            categoryList.forEach(s -> {
                s.setSelected(s.getCategoryId().equals(article.getCategory().getCategoryId()));
            });
            vo.setCategories(categoryList);
            vo.setTags(tagService.queryTagsByCategoryId(article.getCategory().getCategoryId()));
        } else {
            List<CategoryDTO> categoryList = categoryService.loadAllCategories();
            vo.setCategories(categoryList);
            vo.setTags(Collections.emptyList());
        }
        model.addAttribute("vo", vo);
        return "biz/article/edit";
    }


    /**
     * 文章详情页
     * - 参数解析知识点
     * - fixme * [1.Get请求参数解析姿势汇总 | 一灰灰Learning](https://hhui.top/spring-web/01.request/01.190824-springboot%E7%B3%BB%E5%88%97%E6%95%99%E7%A8%8Bweb%E7%AF%87%E4%B9%8Bget%E8%AF%B7%E6%B1%82%E5%8F%82%E6%95%B0%E8%A7%A3%E6%9E%90%E5%A7%BF%E5%8A%BF%E6%B1%87%E6%80%BB/)
     *
     * @param articleId
     * @return
     */
    @GetMapping("detail/{articleId}")
    public String detail(@PathVariable(name = "articleId") Long articleId, Model model) {
        ArticleDetailVo vo = new ArticleDetailVo();
        ArticleDTO articleDTO = articleService.queryTotalArticleInfo(articleId, ReqInfoContext.getReqInfo().getUserId());
        vo.setArticle(articleDTO);

        // 评论信息
        List<TopCommentDTO> comments = commentService.getArticleComments(articleId, PageParam.newPageInstance(1L, 10L));
        vo.setComments(comments);

        // 热门评论
        TopCommentDTO hotComment = commentService.queryHotComment(articleId);
        vo.setHotComment(hotComment);

        // 作者信息
        UserStatisticInfoDTO user = userService.queryUserInfoWithStatistic(articleDTO.getAuthor());
        articleDTO.setAuthorName(user.getUserName());
        articleDTO.setAuthorAvatar(user.getPhoto());
        vo.setAuthor(user);

        // 详情页的侧边推荐信息
        List<SideBarDTO> sideBars = articleRecommendService.recommend(articleDTO);
        vo.setSideBarItems(sideBars);
        model.addAttribute("vo", vo);
        return "biz/article/detail";
    }


}
