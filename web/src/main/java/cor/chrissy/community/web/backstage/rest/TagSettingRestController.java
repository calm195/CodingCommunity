package cor.chrissy.community.web.backstage.rest;

import cor.chrissy.community.common.enums.PushStatEnum;
import cor.chrissy.community.common.enums.StatusEnum;
import cor.chrissy.community.common.req.article.TagReq;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.service.article.service.TagSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx128
 * @createAt 2024/12/19
 */
@RestController
@RequestMapping(path = "backstage/tag/")
public class TagSettingRestController {

    @Autowired
    private TagSettingService tagSettingService;

    @ResponseBody
    @PostMapping(path = "save")
    public Result<String> save(@RequestBody TagReq req) {
        tagSettingService.saveTag(req);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "delete")
    public Result<String> delete(@RequestParam(name = "tagId") Integer tagId) {
        tagSettingService.deleteTag(tagId);
        return Result.ok("ok");
    }

    @ResponseBody
    @GetMapping(path = "operate")
    public Result<String> operate(@RequestParam(name = "tagId") Integer tagId,
                                 @RequestParam(name = "operateType") Integer operateType) {
        if (operateType != PushStatEnum.OFFLINE.getCode() || operateType!= PushStatEnum.ONLINE.getCode()) {
            return Result.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        tagSettingService.operateTag(tagId, operateType);
        return Result.ok("ok");
    }
}
