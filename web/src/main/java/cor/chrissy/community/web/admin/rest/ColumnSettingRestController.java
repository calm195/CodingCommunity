package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.ColumnArticleReq;
import cor.chrissy.community.common.req.article.ColumnReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.dto.ColumnArticleDTO;
import cor.chrissy.community.service.article.dto.ColumnDTO;
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
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = "admin/column/")
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

    @ResponseBody
    @GetMapping(path = "listColumn")
    public Result<PageVo<ColumnDTO>> listColumn(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                                @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<ColumnDTO> columnDTOPageVo = columnSettingService.listColumn(PageParam.newPageInstance(pageNumber, pageSize));
        return Result.ok(columnDTOPageVo);
    }

    @ResponseBody
    @GetMapping(path = "listColumnArticle")
    public Result<PageVo<ColumnArticleDTO>> listColumnArticle(@RequestParam(name = "columnId") Integer columnId,
                                                              @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                                              @RequestParam(name = "pageSize", required = false) Integer pageSize) throws Exception {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        try {
            PageVo<ColumnArticleDTO> simpleArticleDTOS = columnSettingService.queryColumnArticles(
                    columnId, PageParam.newPageInstance(pageNumber, pageSize));
            return Result.ok(simpleArticleDTOS);
        } catch (Exception e) {
            return Result.fail(StatusEnum.COLUMN_QUERY_ERROR, e.getMessage());
        }
    }
}
