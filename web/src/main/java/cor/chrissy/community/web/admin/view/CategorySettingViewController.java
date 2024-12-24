package cor.chrissy.community.web.admin.view;

import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.util.NumUtil;
import cor.chrissy.community.service.article.dto.CategoryDTO;
import cor.chrissy.community.service.article.service.CategorySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/category/")
public class CategorySettingViewController {

    @Autowired
    private CategorySettingService categorySettingService;

    @ResponseBody
    @GetMapping(path = "list")
    public Result<PageVo<CategoryDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<CategoryDTO> categoryDTOPageVo = categorySettingService.getCategoryList(PageParam.newPageInstance(pageNumber.longValue(), pageSize.longValue()));
        return Result.ok(categoryDTOPageVo);
    }
}
