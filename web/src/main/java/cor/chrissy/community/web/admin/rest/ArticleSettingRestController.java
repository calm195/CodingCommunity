package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.OperateArticleEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.article.service.ArticleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
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
}
