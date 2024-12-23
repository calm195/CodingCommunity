package cor.chrissy.community.web.backstage.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.article.ColumnArticleReq;
import cor.chrissy.community.common.req.article.ColumnReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.article.repository.entity.ArticleDO;
import cor.chrissy.community.service.article.service.ArticleReadService;
import cor.chrissy.community.service.article.service.ColumnSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/column/")
public class ColumnSettingRestController {

    @Autowired
    private ColumnSettingService columnSettingService;

    @Autowired
    private ArticleReadService articleReadService;

    @ResponseBody
    @PostMapping(path = "saveColumn")
    public Result<String> saveColumn(@RequestBody ColumnReq req) {
        columnSettingService.saveColumn(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @PostMapping(path = "saveColumnArticle")
    public Result<String> saveColumnArticle(@RequestBody ColumnArticleReq req) {

        // 要求文章必须存在，且已经发布
        ArticleDO articleDO = articleReadService.queryBasicArticle(req.getArticleId());
        if (articleDO == null || articleDO.getStatus() == PushStatEnum.OFFLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在或未发布!");
        }

        columnSettingService.saveColumnArticle(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @PostMapping(path = "sortColumnArticle")
    public Result<String> sortColumnArticle(@RequestBody List<ColumnArticleReq> columnArticleReqs) {
        columnSettingService.sortColumnArticle(columnArticleReqs);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "deleteColumn")
    public Result<String> deleteColumn(@RequestParam(name = "columnId") Integer columnId) {
        columnSettingService.deleteColumn(columnId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "deleteColumnArticle")
    public Result<String> deleteColumnArticle(@RequestParam(name = "id") Integer id) {
        columnSettingService.deleteColumnArticle(id);
        return Result.ok("ok");
    }
}
