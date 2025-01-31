package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.PageParam;
import cor.chrissy.community.common.req.article.CategoryReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.common.vo.PageVo;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
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
@Permission(role = UserRole.LOGIN)
@RequestMapping(path = {"/api/admin/category/", "/admin/category"})
public class CategorySettingRestController {

    @Autowired
    private CategorySettingService categorySettingService;

    @ResponseBody
    @PostMapping(path = "/save")
    @Permission(role = UserRole.ADMIN)
    public Result<String> save(@RequestBody CategoryReq req) {
        categorySettingService.saveCategory(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "/delete")
    @Permission(role = UserRole.ADMIN)
    public Result<String> delete(@RequestParam(name = "categoryId") Integer categoryId) {
        categorySettingService.deleteCategory(categoryId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "/operate")
    @Permission(role = UserRole.ADMIN)
    public Result<String> operate(@RequestParam(name = "categoryId") Integer categoryId,
                                  @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatEnum.OFFLINE.getCode() || pushStatus != PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        categorySettingService.operateCategory(categoryId, pushStatus);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "/list")
    @Permission(role = UserRole.ADMIN)
    public Result<PageVo<CategoryDTO>> list(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        pageNumber = NumUtil.nullOrZero(pageNumber) ? 1 : pageNumber;
        pageSize = NumUtil.nullOrZero(pageSize) ? 10 : pageSize;
        PageVo<CategoryDTO> categoryDTOPageVo = categorySettingService.getCategoryList(PageParam.newPageInstance(pageNumber, pageSize));
        return Result.ok(categoryDTOPageVo);
    }
}

