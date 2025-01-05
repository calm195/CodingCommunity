package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.OperateArticleEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ArticlePostReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.dto.ArticleDTO;
import cor.chrissy.community.service.article.service.ArticleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = "admin/article/")
public class ArticleSettingRestController {

    @Autowired
    private ArticleSettingService articleSettingService;

    @ResponseBody
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "articleId") Long articleId,
                                  @RequestParam(name = "operateType") Integer operateType) {
        OperateArticleEnum operateArticleEnum = OperateArticleEnum.fromCode(operateType);
        if (operateArticleEnum == OperateArticleEnum.EMPTY) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, operateType + "is illegal arguments");
        }
        articleSettingService.operateArticle(articleId, operateArticleEnum);
        return Result.ok("ok");
    }

    @ResponseBody
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody ArticlePostReq req) {
        articleSettingService.updateArticle(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "articleId") Long articleId) {
        articleSettingService.deleteArticle(articleId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "list")
    public Result<PageVo<ArticleDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                           @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<ArticleDTO> articleDTOPageVo = articleSettingService.getArticleList(PageParam.newPageInstance(pageNumber, pageSize));
        return Result.ok(articleDTOPageVo);
    }
}
