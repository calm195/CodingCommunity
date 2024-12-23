package cor.chrissy.community.web.backstage.rest;

import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.article.service.ArticleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/article/")
public class ArticleSettingRestController {

    @Autowired
    private ArticleSettingService articleSettingService;

    @ResponseBody
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "articleId") Long articleId,
                                  @RequestParam(name = "operateType") Integer operateType) {
        // TODO：参数校验
        articleSettingService.operateArticle(articleId, operateType);
        return Result.ok("ok");
    }
}
