package cor.chrissy.community.web.backstage.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.article.CategoryReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.article.service.CategorySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/category/")
public class CategorySettingRestController {

    @Autowired
    private CategorySettingService categorySettingService;

    @ResponseBody
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody CategoryReq req) {
        categorySettingService.saveCategory(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "categoryId") Integer categoryId) {
        categorySettingService.deleteCategory(categoryId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "categoryId") Integer categoryId,
                                 @RequestParam(name = "operateType") Integer operateType) {
        if (operateType != PushStatEnum.OFFLINE.getCode() || operateType!= PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        categorySettingService.operateCategory(categoryId, operateType);
        return Result.ok("ok");
    }
}

