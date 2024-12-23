package cor.chrissy.community.web.backstage.view;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.dto.ColumnDTO;
import cor.chrissy.community.service.article.dto.SimpleArticleDTO;
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
public class ColumnSettingViewController {

    @Autowired
    private ColumnSettingService columnSettingService;

    @ResponseBody
    @GetMapping(path = "listColumn")
    public Result<PageVo<ColumnDTO>> listColumn(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                                @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<ColumnDTO> columnDTOPageVo = columnSettingService.listColumn(PageParam.newPageInstance(pageNumber.longValue(), pageSize.longValue()));
        return Result.ok(columnDTOPageVo);
    }

    @ResponseBody
    @GetMapping(path = "listColumnArticle")
    public Result<List<SimpleArticleDTO>> listColumnArticle(@RequestParam(name = "columnId") Integer columnId) {
        List<SimpleArticleDTO> simpleArticleDTOS = columnSettingService.queryColumnArticles(columnId);
        return Result.ok(simpleArticleDTOS);
    }
}

