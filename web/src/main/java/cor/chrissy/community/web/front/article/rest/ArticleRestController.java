package cor.chrissy.community.web.front.article.rest;

import cor.chrissy.community.common.context.ReqInfoContext;
import cor.chrissy.community.common.enums.DocumentTypeEnum;
import cor.chrissy.community.common.enums.OperateTypeEnum;
import cor.chrissy.community.common.enums.NotifyTypeEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.notify.NotifyMsgEvent;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.NextPageHtmlVo;
import cor.chrissy.community.common.vo.PageListVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.SpringUtil;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.dto.TagDTO;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.service.*;
import cor.chrissy.community.service.user.repository.entity.UserFootDO;
import cor.chrissy.community.service.user.service.UserFootService;
import cor.chrissy.community.web.component.TemplateEngineHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author wx128
 * @createAt 2024/12/18
 */
@RequestMapping(path = "article/api")
@RestController
public class ArticleRestController {
    @Autowired
    private ArticleReadService articleReadService;
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleWriteService articleWriteService;

    @Autowired
    private TemplateEngineHelper templateEngineHelper;

    @Autowired
    private ArticleRecommendService articleRecommendService;

    /**
     * 根据分类 & 标签查询文章列表
     *
     * @param category
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(path = "list")
    public Result<NextPageHtmlVo> list(@RequestParam(value = "category", required = false) String category,
                                      @RequestParam(value = "tag", required = false) String tag,
                                      @RequestParam(name = "page") Long page,
                                      @RequestParam(name = "size", required = false) Long size) {
        if (StringUtils.isBlank(category) && StringUtils.isBlank(tag)) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "category|tag miss!");
        }
        size = Optional.ofNullable(size).orElse(PageParam.DEFAULT_PAGE_SIZE);
        size = Math.min(size, PageParam.DEFAULT_PAGE_SIZE);
        Long categoryId = categoryService.queryCategoryId(category);
        PageListVo<ArticleDTO> articles = articleReadService.queryArticlesByCategory(categoryId, PageParam.newPageInstance(page, size));
        String html = templateEngineHelper.renderToVo("biz/article/list", "articles", articles);
        return Result.ok(new NextPageHtmlVo(html, articles.getHasMore()));
    }

    /**
     * 文章的关联推荐
     *
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(path = "recommend")
    public Result<NextPageHtmlVo> recommend(@RequestParam(value = "articleId") Long articleId,
                                           @RequestParam(name = "page") Long page,
                                           @RequestParam(name = "size", required = false) Long size) {
        size = Optional.ofNullable(size).orElse(PageParam.DEFAULT_PAGE_SIZE);
        size = Math.min(size, PageParam.DEFAULT_PAGE_SIZE);
        PageListVo<ArticleDTO> articles = articleRecommendService.relatedRecommend(articleId, PageParam.newPageInstance(page, size));
        String html = templateEngineHelper.renderToVo("biz/article/list", "articles", articles);
        return Result.ok(new NextPageHtmlVo(html, articles.getHasMore()));
    }

    /**
     * 查询所有的标签
     *
     * @return
     */
    @GetMapping(path = "tag/list")
    public Result<List<TagDTO>> queryTags(Long categoryId) {
        if (categoryId == null || categoryId <= 0L) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS, categoryId);
        }

        List<TagDTO> list = tagService.queryTagsByCategoryId(categoryId);
        return Result.ok(list);
    }

    /**
     * 获取所有的分类
     *
     * @return
     */
    @GetMapping(path = "category/list")
    public Result<List<CategoryDTO>> getCategoryList(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        List<CategoryDTO> list = categoryService.loadAllCategories();
        list.forEach(c -> c.setSelected(c.getCategoryId().equals(categoryId)));
        return Result.ok(list);
    }


    /**
     * 收藏、点赞等相关操作
     *
     * @param articleId
     * @param type      取值来自于 OperateTypeEnum#code
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "favor")
    public Result<Boolean> favor(@RequestParam(name = "articleId") Long articleId,
                                @RequestParam(name = "type") Integer type) {
        OperateTypeEnum operate = OperateTypeEnum.fromCode(type);
        if (operate == OperateTypeEnum.EMPTY) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, type + "非法");
        }

        // 要求文章必须存在
        ArticleDO article = articleReadService.queryBasicArticle(articleId);
        if (article == null) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在!");
        }

        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getAuthorId(),
                ReqInfoContext.getReqInfo().getUserId(),
                operate);
        // 点赞、收藏消息
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(operate);
        Optional.ofNullable(notifyType).ifPresent(notify -> SpringUtil.publishEvent(new NotifyMsgEvent<>(this, notify, foot)));
        return Result.ok(true);
    }


    /**
     * 发布文章，完成后跳转到详情页
     * - 这里有一个重定向的知识点
     * - fixme 博文：* [5.请求重定向 | 一灰灰Learning](https://hhui.top/spring-web/02.response/05.190929-springboot%E7%B3%BB%E5%88%97%E6%95%99%E7%A8%8Bweb%E7%AF%87%E4%B9%8B%E9%87%8D%E5%AE%9A%E5%90%91/)
     *
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "post")
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> post(@RequestBody ArticlePostReq req, HttpServletResponse response) throws IOException {
        Long id = articleWriteService.saveArticle(req, ReqInfoContext.getReqInfo().getUserId());
//        return "redirect:/article/detail/" + id;
//        response.sendRedirect("/article/detail/" + id);
        // 这里采用前端重定向策略
        return Result.ok(id);
    }
}

